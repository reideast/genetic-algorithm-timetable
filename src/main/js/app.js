const React = require('react');
const ReactDOM = require('react-dom');
const when = require('when');
const client = require('./client');
const follow = require('./follow');

const apiRoot = '/api';
const apiGeneticAlgorithmRoot = '/genetic-algorithm-api';

class App extends React.Component {

    constructor(props) {
        super(props);
        this.state = { schedules: [], attributes: [], page: 1, pageSize: 3, links: {} }; // State is data that this component manages; can change over time
        this.updatePageSize = this.updatePageSize.bind(this);
        this.onCreate = this.onCreate.bind(this);
        this.onDelete = this.onDelete.bind(this);
        this.onNavigate = this.onNavigate.bind(this);
    }

    // componentDidMount() {
    //     client({ method: 'GET', path: apiRoot + '/schedule' }).done(response => {
    //         this.setState({ schedules: response.entity._embedded.schedule });
    //     });
    // }
    componentDidMount() {
        this.loadFromServer(this.state.pageSize);
    }

    loadFromServer(pageSize) {
        follow(client, apiRoot, [
            { rel: 'schedule', params: { size: pageSize } }
        ]).then(scheduleCollection => {
            return client({
                method: 'GET',
                path: scheduleCollection.entity._links.profile.href,
                headers: { 'Accept': 'application/schema+json' }
            }).then(schema => {
                // Filter out extra properties of the schema that aren't relevant
                Object.keys(schema.entity.properties).forEach(function(property) {
                    if (schema.entity.properties[property].hasOwnProperty('format') &&
                        schema.entity.properties[property].format === 'uri') {
                        delete schema.entity.properties[property];
                    } else if (schema.entity.properties[property].hasOwnProperty('$ref')) {
                        delete schema.entity.properties[property];
                    }
                });
                this.schema = schema.entity;

                // Save the type of links that a schedule has (i.e. foreign keys)
                this.links = scheduleCollection.entity._links;

                return scheduleCollection;
            });
        }).then(scheduleCollection => {
            this.page = scheduleCollection.entity.page;
            // get the ACTUAL schedule objects, no the "stub" _embedded
            return scheduleCollection.entity._embedded.schedule.map(schedule =>
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
                pageSize: pageSize,
                links: this.links
            });
        });
    }

    onCreate(newSchedule) {
        follow(client, apiRoot, ['schedule']).then(scheduleCollection => {
            return client({
                method: 'POST',
                path: scheduleCollection.entity._links.self.href,
                entity: newSchedule,
                headers: { 'Content-Type': 'application/json' }
            });
        }).then(response => {
            return follow(client, apiRoot, [{ rel: 'schedule', params: { 'size': this.state.pageSize } }]);
        }).done(response => {
            // After creating a new entity, go to the last page so the new item is visible
            if (typeof response.entity._links.last !== 'undefined') {
                this.onNavigate(response.entity._links.last.href);
            } else {
                this.onNavigate(response.entity._links.self.href);
            }
        });
    }

    onNavigate(navUri) {
        // Go to "first", "next", "last", etc. based on REST _links
        client({ method: 'GET', path: navUri }).done(scheduleCollection => {
            this.setState({
                schedules: scheduleCollection.entity._embedded.schedule,
                attributes: this.state.attributes,
                pageSize: this.state.pageSize,
                links: scheduleCollection.entity._links
            });
        });
    }

    onDelete(employee) {
        client({ method: 'DELETE', path: employee._links.self.href }).done(response => {
            this.loadFromServer(this.state.pageSize);
        });
    }

    updatePageSize(inputtedPageSize) {
        if (inputtedPageSize !== this.state.pageSize) {
            this.loadFromServer(inputtedPageSize);
        }
    }

    render() {
        return (
            <div>
                <RunGeneticAlgorithmDialog
                    attributes={this.state.attributes}
                    scheduleId={3}
                />
                {/*DEBUG temporarily disable all the schedule stuff*/}
                {/*<CreateDialog attributes={this.state.attributes} onCreate={this.onCreate} />*/}
                {/*<ScheduleList schedules={this.state.schedules}*/}
                {/*links={this.state.links}*/}
                {/*page={this.state.page}*/}
                {/*pageSize={this.state.pageSize}*/}
                {/*attributes={this.state.attributes}*/}
                {/*onNavigate={this.onNavigate}*/}
                {/*onDelete={this.onDelete}*/}
                {/*updatePageSize={this.updatePageSize} />*/}
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
        //     return follow(client, apiRoot, [{ rel: 'schedule', params: { 'size': this.state.pageSize } }]);
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
                <a href="#createJob">Create</a>
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
                      attribtes={this.props.attributes}
                      onDelete={this.props.onDelete} />
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
                    <tbody>
                        <tr>
                            <th>Creator</th>
                            <th>ScheduleId</th>
                            <th>CreationDate</th>
                            <th>IsMaster</th>
                            <th>ScheduledModules</th>
                            <th></th>
                        </tr>
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
        this.props.onDelete(this.props.schedule);
    }

    render() {
        return (
            <tr>
                <td>{this.props.schedule.creator}</td>
                <td>{this.props.schedule.scheduleId}</td>
                <td>{this.props.schedule.creationDate}</td>
                <td>{this.props.schedule.master ? 'true' : 'false'}</td>
                <td>scheduledModules</td>
                <td>
                    <button onClick={this.handleDelete}>Delete</button>
                </td>
            </tr>
        );
    }
}

ReactDOM.render(
    <App />,
    document.getElementById('react')
);
