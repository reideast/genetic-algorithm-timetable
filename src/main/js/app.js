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

// TODO: need a REST controller for the root of /genetic-algorithm-api, which returns metadata for all possible endpoints. How does spring-data do it?

class App extends React.Component {
    constructor(props) {
        super(props);
        // this.state = {
            // loggedInUser: this.props.loggedInUser // Note: using this.props to set initial state in the constructor can be an anti-pattern: since the constructor is only called ONCE but the Component can be re-rendered with different props, must make sure that any props saved here are meant to be UNCHANGEABLE
        // };
    }

    render() {
        return (
            <div>
                <SchedulingJobLauncher loggedInUser={this.props.loggedInUser} />
            </div>
        );
    }
}

class SchedulingJobLauncher extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            currentTimetableSchedule: [],
            jobRunning: undefined
        };
        this.onJob = this.onJob.bind(this);
    }

    /**
     * After a job has been started, this signal will be received
     * Update this Component's state, so that the {@link Timetable} will begin updating
     * @param schedule
     */
    onJob(schedule) {
        console.log('STUB ON JOB', schedule.entity.scheduleId); // DEBUG
        this.setState({
            currentTimetableSchedule: [schedule],
            jobRunning: undefined // FUTURE: Status updates
        });
    }

    render() {
        return (
            <div>
                <AvailableSchedulesTable loggedInUser={this.props.loggedInUser}
                                         onJob={this.onJob} />
                <TimetableFetcher loggedInUser={this.props.loggedInUser}
                                  schedule={this.state.currentTimetableSchedule} />
            </div>
        );
    }
}

// Required to deal with SchedulingJobLauncher.state.currentTimetableSchedule being able to be undefined (or, to fit this idiom, []) (unless there's a better idiom)
// TODO: I _think_ this can be collapsed down. I'm understanding state better now
class TimetableFetcher extends React.Component {
    render() {
        if (this.props.schedule.length < 1) {
            return null; // Do not render until a schedule has been given
        }

        if (this.props.scheduleId.left > 1) {
            console.error("Assertion error: Schedule job launcher was not meant to send more than one schedule at once to show Timetable of")
        }

        // Now that schedule's length as 1 has been asserted, don't need to loop over the array
        console.log('making timetable out of schedule:', schedule[0]); // DEBUG
        return (
            <div>
                <Timetable loggedInUser={this.props.loggedInUser}
                           schdule={this.props.schedule[0]} />
            </div>
        );
    }
}

class Timetable extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            scheduledModules: [],
        };
        this.refreshTimetableAfterEvent = this.refreshTimetableAfterEvent.bind(this);
    }

    loadFromServer() {
        follow(client, apiRoot, [
            'scheduledModules',
            'search',
            { rel: 'schedule', params: { id: this.props.schedule.entity.scheduleId } }
        ]).then(scheduledModulesCollection => {
            scheduledModulesCollection.entity._embedded.scheduledModules.forEach((item) => {
                console.log('is this a scheduled module?', item);
            });

            return scheduledModulesCollection.entity._embedded.scheduledModules.map(scheduledModule => {
                console.log(scheduledModule._links.module.href);
                return client({
                    method: 'GET',
                    path: scheduledModule._links.module.href
                });
            });
        }).then(promises => {
            return when.map(promises, (x, i) => {
                console.log('map', x, i);
            });
        }).done(schedules => {
            this.setState({
                schedules: schedules // an array: each item: obj, with obj.entity (obj.entity.scheduleId, obj.entity.master, etc). Also, obj.entity.creator (type Promise)
            });
        });
    }

    refreshTimetableAfterEvent(message) {
        follow(client, apiRoot, ['schedules']).then(schedulesCollection => {
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
                schedules: schedules
            });
        });
    }

    componentDidMount() {
        // DEBUG: THIS ISN"T HOW IT SHOULD WORK: Maybe need a guard here? `if (!this.props.schedule)` then do nothing
        this.loadFromServer();
        // When WebSockets broker sends us back these events, then perform these callback actions
        // This is how the data is re-loaded after an object is updated in the DB
        // TODO: in EventHandler.java, add topics for this component
        stompClient.register([
            { route: '/topic/newSchedule', callback: this.refreshTimetableAfterEvent },
            { route: '/topic/updateSchedule', callback: this.refreshTimetableAfterEvent },
            { route: '/topic/deleteSchedule', callback: this.refreshTimetableAfterEvent }
        ]);
    }

    render() {
        return (
            <div>
                {this.state.scheduledModules}
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
        };
        this.refreshPageOfSchedules = this.refreshPageOfSchedules.bind(this);
    }

    loadFromServer() {
        // Get all schedules that were created by this logged-in user
        follow(client, apiRoot, [
            'schedules',
            'search',
            { rel: 'creatorUsername', params: { username: this.props.loggedInUser } }
        ]).then(scheduleCollection => {
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
                schedules: schedules // an array: each item: obj, with obj.entity (obj.entity.scheduleId, obj.entity.master, etc). Also, obj.entity.creator (type Promise)
            });
        });
    }

    refreshPageOfSchedules(message) {
        follow(client, apiRoot, ['schedules']).then(schedulesCollection => {
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
                schedules: schedules
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
                <ScheduleTable loggedInUser={this.props.loggedInUser}
                               schedules={this.state.schedules}
                               onJob={this.props.onJob} />
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

        // Can't actually use follow.js: we're making a POST request: follow(client, apiGeneticAlgorithmRoot, ['job'])
        // TODO: Once genetic-algorithm-api root has an endpoint that returns all other endpoints, we can use follow to get the endpoint, then will still use client() to make the POST. See: https://github.com/spring-guides/tut-react-and-spring-data-rest/blob/master/security/src/main/js/app.js#L81
        client({
            method: 'POST',
            path: apiGeneticAlgorithmRoot + '/job',
            params: { scheduleId: this.props.schedule.entity.scheduleId }
        }).done(response => {
            console.log('Job submitted, response received:', response); // DEBUG
            // TODO: Do something with this job JSON object?
            this.props.onJob(this.props.schedule);
            // TODO: UI Message. Actually, probably not needed, since the dialog box should close and then show the <Timetable>
            window.location = '#'; // Close dialog box
        });
    }

    render() {
        return (
            <div>
                <a href={'#createJob-' + this.props.schedule.entity.scheduleId}>Start genetic algorithm</a>
                <div id={'createJob-' + this.props.schedule.entity.scheduleId} className="modalDialog">
                    <div>
                        <a href="#" title="Close" className="close">X</a>
                        <h2>Create a genetic algorithm job</h2>
                        <p><em>scheduleId={this.props.schedule.entity.scheduleId}</em></p>
                        <button onClick={this.handleSubmit}>Create Job</button>
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
            return (
                <Schedule key={schedule.entity.scheduleId}
                          loggedInUser={this.props.loggedInUser}
                          schedule={schedule}
                          onJob={this.props.onJob} />
            );
        });

        return (
            <div>
                <table>
                    <thead>
                        <tr>
                            <th>ScheduleId</th>
                            <th>Creator</th>
                            <th>Created On</th>
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
        this.state = {
            creator: {}
        };
    }

    loadFromServer() {
        // Get creator of this schedule
        // this REST call returns an actual object, no double-calling through the _embedded is needed
        client({
            method: 'GET',
            path: this.props.schedule.entity._links.creator.href
        }).done(creator => {
            this.setState({
                creator: creator
            });
        });
    }

    componentDidMount() {
        this.loadFromServer();
    }

    render() {
        const creationDate = new Date(this.props.schedule.entity.creationDate);
        return (
            <tr>
                <td>{this.props.schedule.entity.scheduleId}</td>
                <td>{(this.state.creator.entity) ? this.state.creator.entity.displayName : null}</td>
                <td>{creationDate.toLocaleDateString()}</td>
                <td>
                    <RunGeneticAlgorithmDialog key={this.props.schedule.entity.scheduleId}
                                               loggedInUser={this.props.loggedInUser}
                                               onJob={this.props.onJob}
                                               schedule={this.props.schedule} />
                </td>
            </tr>
        );
    }
}

ReactDOM.render(
    <App loggedInUser={document.getElementById('loggedInUser').innerHTML} />,
    document.getElementById('react')
);
