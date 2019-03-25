/**
 * Based on work by Greg Turnquist
 * See: https://spring.io/guides/tutorials/react-and-spring-data-rest/
 */
// Original license is:
/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
const React = require('react');
const ReactDOM = require('react-dom');
const when = require('when');
const client = require('./client');
const follow = require('./follow');
const stompClient = require('./websocket-listener');

const apiRoot = '/api';
const apiGeneticAlgorithmRoot = '/genetic-algorithm-api';

class App extends React.Component {

    constructor(props) {
        super(props);
        // State is data that this component manages; can change over time
        this.state = {
            schedules: [],
            attributes: [],
            page: 1, pageSize: 3,
            links: {},
            loggedInUser: this.props.loggedInUser
        }; // this.props.loggedInUser comes from when the App object is created <App loggedInUser="..."
        this.updatePageSize = this.updatePageSize.bind(this);
        this.onCreate = this.onCreate.bind(this);
        this.onUpdate = this.onUpdate.bind(this);
        this.onDelete = this.onDelete.bind(this);
        this.onNavigate = this.onNavigate.bind(this);
        this.refreshCurrentPage = this.refreshCurrentPage.bind(this);
        this.refreshAndGoToLastPage = this.refreshAndGoToLastPage.bind(this);
    }

    loadFromServer(pageSize) {
        follow(client, apiRoot, [
            { rel: 'schedules', params: { size: pageSize } }
        ]).then(scheduleCollection => {
            return client({
                method: 'GET',
                path: scheduleCollection.entity._links.profile.href,
                headers: { 'Accept': 'application/schema+json' }
            }).then(schema => {
                // Filter out extra properties of the schema that aren't relevant, e.g. URI references, subtypes
                Object.keys(schema.entity.properties).forEach(function(property) {
                    if (schema.entity.properties[property].hasOwnProperty('format') &&
                        schema.entity.properties[property].format === 'uri') {
                        delete schema.entity.properties[property];
                    } else if (schema.entity.properties[property].hasOwnProperty('$ref')) {
                        delete schema.entity.properties[property];
                    }
                });

                // Save schema
                this.schema = schema.entity;

                // Save the type of links that a schedule has (i.e. foreign keys)
                this.links = scheduleCollection.entity._links;

                return scheduleCollection;
            });
        }).then(scheduleCollection => {
            this.page = scheduleCollection.entity.page;

            // get the ACTUAL schedule objects (with ETag version number), not the "stub" _embedded
            return scheduleCollection.entity._embedded.schedules.map(schedule =>
                client({
                    method: 'GET',
                    path: schedule._links.self.href
                })
            );
        }).then(schedulePromises => {
            return when.all(schedulePromises);
        }).done(schedules => {
            this.setState({
                page: this.page, // DEBUG: not seeing page|schema|links on this. in the console
                schedules: schedules, // an array: each item: obj, with obj.entity (obj.entity.scheduleId, obj.entity.master, etc). Also, obj.entity.creator (type Promise)
                attributes: Object.keys(this.schema.properties),
                pageSize: pageSize,
                links: this.links
            });
        });
    }

    onCreate(newSchedule) {
        follow(client, apiRoot, ['schedules']).done(response => {
            client({
                method: 'POST',
                path: response.entity._links.self.href,
                entity: newSchedule,
                headers: { 'Content-Type': 'application/json' }
            });
        });
    }

    // onCreate(newSchedule) {
    //     follow(client, apiRoot, ['schedules']).then(scheduleCollection => {
    //         return client({
    //             method: 'POST',
    //             path: scheduleCollection.entity._links.self.href,
    //             entity: newSchedule,
    //             headers: { 'Content-Type': 'application/json' }
    //         });
    //     }).then(response => {
    //         return follow(client, apiRoot, [{ rel: 'schedules', params: { 'size': this.state.pageSize } }]);
    //     }).done(response => {
    //         // After creating a new entity, go to the last page so the new item is visible
    //         if (typeof response.entity._links.last !== 'undefined') {
    //             this.onNavigate(response.entity._links.last.href);
    //         } else {
    //             this.onNavigate(response.entity._links.self.href);
    //         }
    //     });
    // }

    onUpdate(schedule, updatedSchedule) {
        // FUTURE: Could use something like this to check user roles, etc.: if (schedule.entity.manager.name === this.state.loggedInManager) {
        // FUTURE:    alert('You are not authorized to update');
        // FUTURE: } else {
        console.log(updatedSchedule['owner']);
        console.log(schedule.entity.owner);
        // updatedSchedule['manager'] = schedule.entity.manager;
        // TODO: Need to find current user from the API, then then add them as schedule.entity.owner
        client({
            method: 'PUT',
            path: schedule.entity._links.self.href,
            entity: updatedSchedule,
            headers: {
                'Content-Type': 'application/json',
                'If-Match': schedule.headers.Etag
            }
        }).done(response => {
            /* Do not update the UI manually. Let the websocket handle updating the UI eventually (though will be soon) */
        }, errorResponse => {
            if (errorResponse.status.code === 403) {
                alert('ACCESS DENIED: You are not authorized to update ' +
                    schedule.entity._links.self.href);
            }
            if (errorResponse.status.code === 412) {
                alert('DENIED: Unable to update ' + schedule.entity._links.self.href +
                    '. Your copy is stale.');
            }
        });
    }

    onNavigate(navUri) {
        // Go to "first", "next", "last", etc. based on REST _links
        client({
            method: 'GET',
            path: navUri
        }).then(scheduleCollection => {
            this.links = scheduleCollection.entity._links;
            this.page = scheduleCollection.entity.page;

            return scheduleCollection.entity._embedded.schedules.map(schedule =>
                client({
                    method: 'GET',
                    path: schedule._links.self.href
                })
            );
        }).then(schedulePromises => {
            return when.all(schedulePromises);
        }).done(schedules => {
            this.setState({
                page: this.page,
                schedules: schedules,
                attributes: Object.keys(this.schema.properties),
                pageSize: this.state.pageSize,
                links: this.links
            });
        });
    }

    onDelete(schedule) {
        client({
            method: 'DELETE',
            path: schedule.entity._links.self.href
        }).done(response => {
            /* Do not update the UI manually. Let the websocket handle updating the UI eventually (though will be soon) */
        }, errorResponse => {
            if (errorResponse.status.code === 403) {
                alert('ACCESS DENIED: You are not authorized to delete ' +
                    schedule.entity._links.self.href);
            }
        });
    }

    updatePageSize(inputtedPageSize) {
        if (inputtedPageSize !== this.state.pageSize) {
            this.loadFromServer(inputtedPageSize);
        }
    }

    refreshAndGoToLastPage(message) {
        follow(client, apiRoot, [{
            rel: 'schedules',
            params: { size: this.state.pageSize }
        }]).done(response => {
            if (response.entity._links.last !== undefined) {
                this.onNavigate(response.entity._links.last.href);
            } else {
                this.onNavigate(response.entity._links.self.href);
            }
        });
    }

    refreshCurrentPage(message) {
        follow(client, apiRoot, [{
            rel: 'schedules',
            params: {
                size: this.state.pageSize,
                page: this.state.page.number
            }
        }]).then(schedulesCollection => {
            this.links = schedulesCollection.entity._links;
            this.page = schedulesCollection.entity.page;

            return schedulesCollection.entity._embedded.schedules.map(schedule => {
                return client({
                    method: 'GET',
                    path: schedule._links.self.href
                });
            });
        }).then(schedulesPromises => {
            return when.all(schedulesPromises);
        }).then(schedules => {
            this.setState({
                page: this.page,
                schedules: schedules,
                attributes: Object.keys(this.schema.properties),
                pageSize: this.state.pageSize,
                links: this.links
            });
        });
    }

    componentDidMount() {
        this.loadFromServer(this.state.pageSize);
        // When WebSockets broker sends us back these events, then perform these callback actions
        // This is how the data is re-loaded after an object is updated in the DB
        stompClient.register([
            { route: '/topic/newSchedule', callback: this.refreshAndGoToLastPage },
            { route: '/topic/updateSchedule', callback: this.refreshCurrentPage },
            { route: '/topic/deleteSchedule', callback: this.refreshCurrentPage }
        ]);
    }

    render() {
        return (
            <div>
                <RunGeneticAlgorithmDialog
                    attributes={this.state.attributes}
                    scheduleId={3}
                />
                <CreateDialog attributes={this.state.attributes} onCreate={this.onCreate} />
                <ScheduleList schedules={this.state.schedules}
                              links={this.state.links}
                              page={this.state.page}
                              pageSize={this.state.pageSize}
                              attributes={this.state.attributes}
                              onNavigate={this.onNavigate}
                              onUpdate={this.onUpdate}
                              onDelete={this.onDelete}
                              updatePageSize={this.updatePageSize}
                              loggedInUser={this.state.loggedInUser} />
            </div>

        );
    }
}

class RunGeneticAlgorithmDialog extends React.Component {
    constructor(props) {
        super(props);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleSubmit(e) {
        e.preventDefault();

        console.log(this.props, 'scheduleId=', this.props.scheduleId);

        client({
            method: 'POST',
            path: apiGeneticAlgorithmRoot + '/job',
            params: { scheduleId: this.props.scheduleId }
            // }).then(response => {
            //     return follow(client, apiRoot, [{ rel: 'schedules', params: { 'size': this.state.pageSize } }]);
        }).done(response => {
            console.log(response);
            // // After creating a new entity, go to the last page so the new item is visible
            // if (typeof response.entity._links.last !== 'undefined') {
            //     this.onNavigate(response.entity._links.last.href);
            // } else {
            //     this.onNavigate(response.entity._links.self.href);
            // }
        });
    }

    render() {
        return (
            <div>
                <a href="#createJob">Create a genetic algorithm job</a>
                <div id="createJob" className="modalDialog">
                    <div>
                        <a href="#" title="Close" className="close">X</a>
                        <h2>Create a scheduling job</h2>
                        <form>
                            <button onClick={this.handleSubmit}>Create</button>
                        </form>
                    </div>
                </div>
            </div>
        );
    }
}

class CreateDialog extends React.Component {
    constructor(props) {
        super(props);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleSubmit(e) {
        e.preventDefault();

        // Create new Object with form <input>'s
        const newSchedule = {};
        this.props.attributes.forEach(attribute => {
            newSchedule[attribute] = ReactDOM.findDOMNode(this.refs[attribute]).value.trim();
            // this.refs is grabbing THIS React component
        });
        this.props.onCreate(newSchedule);

        // Clear this dialog's <input>'s
        this.props.attributes.forEach(attribute => {
            ReactDOM.findDOMNode(this.refs[attribute].value = '');
        });

        // Navigate away
        window.location = '#';
    }

    render() {
        // Set up an array of objects (via map) that will be auto-built from the Schema found via REST /profile/ of type Accept:application/schema+json
        const inputs = this.props.attributes.map(attribute => // Note the lack of a "{" here: this function body only has one statement, so it RETURNS a SINGLE OBJECT, a JSX obj
            <p key={attribute}>
                <input type="text" placeholder={attribute} ref={attribute} className="field" />
            </p>
        );

        return (
            <div>
                <a href="#createSchedule">Create</a>
                <div id="createSchedule" className="modalDialog">
                    <div>
                        <a href="#" title="Close" className="close">X</a>
                        <h2>Create new schedule</h2>
                        <form>
                            {inputs}
                            <button onClick={this.handleSubmit}>Create</button>
                        </form>
                    </div>
                </div>
            </div>
        );
    }
}

class UpdateDialog extends React.Component {
    constructor(props) {
        super(props);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleSubmit(e) {
        e.preventDefault();
        const updatedSchedule = {};
        this.props.attributes.forEach(attribute => {
            updatedSchedule[attribute] = ReactDOM.findDOMNode(this.refs[attribute]).value.trim();
        });
        this.props.onUpdate(this.props.schedule, updatedSchedule);
        window.location = '#';
    }

    render() {
        const inputs = this.props.attributes.map(attribute =>
            <p key={attribute}>
                {/*FUTURE: Potential pull request for tutorial: key should be {attribute}, not the value of an attribute. When `typeof attribute` is a boolean, there can easily be duplicates*/}
                <input type="text" placeholder={attribute}
                       defaultValue={this.props.schedule.entity[attribute]}
                       ref={attribute} className="field" />
            </p>
        );

        const dialogId = 'updateSchedule-' + this.props.schedule.entity._links.self.href;

        // FUTURE: This is a way to authorize updating
        // const isManagerCorrect = this.props.employee.entity.manager.name == this.props.loggedInManager;
        // if (isManagerCorrect === false) {
        //     return (
        //         <div>
        //             <a>Not Your Employee</a>
        //         </div>
        //     );
        // } else {
        return (
            <div>
                <a href={'#' + dialogId}>Update</a>
                <div id={dialogId} className="modalDialog">
                    <div>
                        <a href="#" title="Close" className="close">X</a>
                        <h2>Update schedule</h2>
                        <form>
                            {inputs}
                            <button onClick={this.handleSubmit}>Update</button>
                        </form>
                    </div>
                </div>
            </div>
        );
        // }
    }
}

class ScheduleList extends React.Component {
    constructor(props) {
        super(props);
        this.handleNavFirst = this.handleNavFirst.bind(this);
        this.handleNavPrev = this.handleNavPrev.bind(this);
        this.handleNavNext = this.handleNavNext.bind(this);
        this.handleNavLast = this.handleNavLast.bind(this);
        this.handlePageSizeInput = this.handlePageSizeInput.bind(this);
    }

    render() {
        const pageInfo = this.props.page.hasOwnProperty('number') ?
            <h3>Schedules - Page {this.props.page.number + 1} of {this.props.page.totalPages}</h3> : null;

        const schedules = this.props.schedules.map(schedule =>
            <Schedule key={schedule.entity._links.self.href}
                      schedule={schedule}
                      attributes={this.props.attributes}
                      onUpdate={this.props.onUpdate}
                      onDelete={this.props.onDelete}
                      loggedInUser={this.props.loggedInUser} />
        );

        const navLinks = [];
        if ('first' in this.props.links) {
            navLinks.push(<button key="first" onClick={this.handleNavFirst}>&lt;&lt;</button>);
        }
        if ('prev' in this.props.links) {
            navLinks.push(<button key="prev" onClick={this.handleNavPrev}>&lt;</button>);
        }
        if ('next' in this.props.links) {
            navLinks.push(<button key="next" onClick={this.handleNavNext}>&gt;</button>);
        }
        if ('last' in this.props.links) {
            navLinks.push(<button key="last" onClick={this.handleNavLast}>&gt;&gt;</button>);
        }

        return (
            <div>
                {pageInfo}
                <input ref="pageSize" defaultValue={this.props.pageSize} onInput={this.handlePageSizeInput} />
                <table>
                    <thead>
                        <tr>
                            <th>Creator</th>
                            <th>ScheduleId</th>
                            <th>CreationDate</th>
                            <th>IsMaster</th>
                            <th>ScheduledModules</th>
                            <th></th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        {schedules}
                    </tbody>
                </table>
                <div>
                    {navLinks}
                </div>
            </div>
        );
    }

    handleNavFirst(e) {
        e.preventDefault();
        this.props.onNavigate(this.props.links.first.href);
    }

    handleNavPrev(e) {
        e.preventDefault();
        this.props.onNavigate(this.props.links.prev.href);
    }

    handleNavNext(e) {
        e.preventDefault();
        this.props.onNavigate(this.props.links.next.href);
    }

    handleNavLast(e) {
        e.preventDefault();
        this.props.onNavigate(this.props.links.last.href);
    }

    handlePageSizeInput(e) {
        e.preventDefault();
        const inputtedPageSize = ReactDOM.findDOMNode(this.refs.pageSize).value;
        if (/^[0-9]+$/.test(inputtedPageSize)) {
            this.props.updatePageSize(inputtedPageSize);
        } else {
            ReactDOM.findDOMNode(this.refs.pageSize).value = inputtedPageSize.substring(0, inputtedPageSize.length - 1);
        }
    }
}

class Schedule extends React.Component {
    constructor(props) {
        super(props);
        this.handleDelete = this.handleDelete.bind(this);
    }

    handleDelete() {
        this.props.onDelete(this.props.schedules);
    }

    render() {
        return (
            <tr>
                <td>{this.props.schedule.entity.creator.username}</td>
                {/*<td>TODO: creator</td>*/}
                <td>{this.props.schedule.entity.scheduleId}</td>
                <td>{this.props.schedule.entity.creationDate}</td>
                <td>{this.props.schedule.entity.master ? 'true' : 'false'}</td>
                <td>TODO: scheduledModules</td>
                <td>
                    <UpdateDialog schedule={this.props.schedule}
                                  attributes={this.props.attributes}
                                  onUpdate={this.props.onUpdate}
                                  loggedInUser={this.props.loggedInUser} />
                </td>
                <td>
                    <button onClick={this.handleDelete}>Delete</button>
                </td>
            </tr>
        );
    }
}

ReactDOM.render(
    <App loggedInUser={document.getElementById('loggedInUser').innerHTML} />,
    document.getElementById('react')
);
