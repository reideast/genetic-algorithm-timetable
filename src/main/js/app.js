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
        this.state = {
            loggedInUser: this.props.loggedInUser
        };
    }

    render() {
        return (
            <div>
                <SchedulingJobLauncher loggedInUser={this.state.loggedInUser} />
            </div>
        );
    }
}

class SchedulingJobLauncher extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            loggedInUser: this.props.loggedInUser
        };
        this.onJob = this.onJob.bind(this);
    }

    onJob(foo) {

    }

    render() {
        return (
            <div>
                <AvailableSchedulesTable loggedInUser={this.state.loggedInUser}
                                         onJob={this.onJob} />
            </div>
        );
    }
}

class AvailableSchedulesTable extends React.Component {

    constructor(props) {
        super(props);
        // State is data that this component manages; can change over time
        this.state = {
            schedules: [],
            links: {},
            loggedInUser: this.props.loggedInUser
        }; // this.props.loggedInUser comes from when the App object is created <App loggedInUser="..."
        this.refreshPageOfSchedules = this.refreshPageOfSchedules.bind(this);
    }

    loadFromServer() {
        follow(client, apiRoot, ['schedules']).then(scheduleCollection => {
            // TODO: follow(client, apiRoot, ['schedules/search/creatorUsername/' + this.props.loggedInUser]).then(scheduleCollection => {
            // Save the type of links that a schedule has (i.e. first, next, search, profile)
            this.links = scheduleCollection.entity._links;

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
                schedules: schedules, // an array: each item: obj, with obj.entity (obj.entity.scheduleId, obj.entity.master, etc). Also, obj.entity.creator (type Promise)
                links: this.links
            });
        });
    }

    refreshPageOfSchedules(message) {
        follow(client, apiRoot, [ 'schedules' ]).then(schedulesCollection => {
            this.links = schedulesCollection.entity._links;

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
                schedules: schedules,
                links: this.links
            });
        });
    }

    componentDidMount() {
        this.loadFromServer();
        // When WebSockets broker sends us back these events, then perform these callback actions
        // This is how the data is re-loaded after an object is updated in the DB
        stompClient.register([
            { route: '/topic/newSchedule', callback: this.refreshPageOfSchedules },
            { route: '/topic/updateSchedule', callback: this.refreshPageOfSchedules },
            { route: '/topic/deleteSchedule', callback: this.refreshPageOfSchedules }
        ]);
    }

    render() {
        return (
            <div>
                <ScheduleTable loggedInUser={this.state.loggedInUser}
                               schedules={this.state.schedules}
                               links={this.state.links}
                               onJob={this.onJob} />
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
        }).done(response => {
            console.log('Job submitted, response received:', response);
            // TODO: UI Message
            window.location = '#';
        });
    }

    render() {
        return (
            <div>
                <a href="#createJob">Start genetic algorithm</a>
                <div id="createJob" className="modalDialog">
                    <div>
                        <a href="#" title="Close" className="close">X</a>
                        <h2>Create a genetic algorithm job</h2>
                        <form>
                            <button onClick={this.handleSubmit}>Create</button>
                        </form>
                    </div>
                </div>
            </div>
        );
    }
}

class ScheduleTable extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        const schedules = this.props.schedules.map(schedule => {
            // console.log("making a schedule row:", schedule);
            return (
                <Schedule key={schedule.entity._links.self.href}
                          loggedInUser={this.props.loggedInUser}
                          schedule={schedule}
                          onJob={this.props.onJob} />
            )
            }
        );

        return (
            <div>
                <table>
                    <thead>
                        <tr>
                            <th>ScheduleId</th>
                            <th>Creator</th>
                            <th>CreationDate</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        {schedules}
                    </tbody>
                </table>
            </div>
        );
    }
}

class Schedule extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        // this.props.schedule.entity.creator = this.props.schedule.entity.creator.done();
        // console.log("in the row, id=", this.props.schedule.entity.scheduleId);

        return (
            <tr>
                <td>{this.props.schedule.entity.scheduleId}</td>
                <td>{this.props.schedule.entity.creator.username}</td>
                <td>{this.props.schedule.entity.creationDate}</td>
                <td>
                    <RunGeneticAlgorithmDialog key={this.props.schedule.entity.scheduleId}
                                               loggedInUser={this.props.loggedInUser}
                                               onJob={this.props.onJob}
                                               scheduleId={this.props.schedule.entity.scheduleId} />
                </td>
            </tr>
        );
    }
}

ReactDOM.render(
    <App loggedInUser={document.getElementById('loggedInUser').innerHTML} />,
    document.getElementById('react')
);
