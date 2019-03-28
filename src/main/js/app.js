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

import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {faDna, faUser, faMicrochip, faSpinner} from '@fortawesome/free-solid-svg-icons';

import './sass/style.scss';
import 'react-bootstrap/dist/react-bootstrap.min.js';
import 'bootstrap/dist/js/bootstrap.bundle.min';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Button from 'react-bootstrap/Button';
import Table from 'react-bootstrap/Table';
import Tabs from 'react-bootstrap/Tabs';

const apiRoot = '/api';
const apiGeneticAlgorithmRoot = '/genetic-algorithm-api';

// TODO: need a REST controller for the root of /genetic-algorithm-api, which returns metadata for all possible endpoints. How does spring-data do it?

class App extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <Container className="bg-white">
                <div className="px-3 py-3 pt-md-5 pb-md-4 mx-auto text-center">
                    <h1>Run Genetic Algorithm</h1>
                </div>
                <SchedulingJobLauncher loggedInUser={this.props.loggedInUser} />
            </Container>
        );
    }
}

class SchedulingJobLauncher extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            currentTimetableSchedule: [],
            localDisplayedVersion: -1,
            jobRunning: undefined
        };
        this.onJob = this.onJob.bind(this);
    }

    /**
     * After a job has been started, this signal will be received
     * Update this Component's state, so that the {@link Timetable} will begin updating
     * @param schedule
     */
    onJob(jobId, totalGenerations, startDateString, schedule) {
        // TODO: have gotten jobId, generations, and start date. Display them!
        console.log('STUB ON JOB', schedule.entity.scheduleId); // DEBUG
        this.setState(previousState => ({
            currentTimetableSchedule: [schedule],
            localDisplayedVersion: previousState.localDisplayedVersion + 1,
            jobRunning: undefined // FUTURE: Status updates
        }));
    }

    render() {
        let timetable = null;
        if (this.state.currentTimetableSchedule.length > 0) { // Do not render until a schedule has been given
            if (this.state.currentTimetableSchedule.length > 1) {
                console.error('Assertion error: Schedule job launcher was not meant to create more than one schedule at once, and only the first one will be displayed');
            }
            // Now that schedule's length as 1 has been asserted, don't need to loop over the array
            console.log('making timetable out of schedule:', this.state.currentTimetableSchedule[0]); // DEBUG
            timetable = (
                <Timetable loggedInUser={this.props.loggedInUser}
                           schedule={this.state.currentTimetableSchedule[0]}
                           localDisplayedVersion={this.state.localDisplayedVersion} />
            );
        }

        return (
            <div>
                <AvailableSchedulesTable loggedInUser={this.props.loggedInUser}
                                         onJob={this.onJob} />
                {timetable}
            </div>
        );
    }
}

class Timetable extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            currentDisplayedVersion: -1,
            scheduledModules: [],
            fetchDoneWhenZero: -1,
            visibleTimetable: -1
        };
        this.refreshTimetableAfterEvent = this.refreshTimetableAfterEvent.bind(this);
    }

    loadFromServer() {
        if (this.state.currentDisplayedVersion === this.props.localDisplayedVersion) {
            return; // This schedule has already been fetched
        }
        this.setState({
            currentDisplayedVersion: this.props.localDisplayedVersion,
            scheduledModules: []
        });

        follow(client, apiRoot, [
            'scheduledModules',
            'search',
            { rel: 'schedule', params: { id: this.props.schedule.entity.scheduleId } }
        ]).done(scheduledModulesCollection => {
            this.setState({
                fetchDoneWhenZero: scheduledModulesCollection.entity._embedded.scheduledModules.length
            });
            scheduledModulesCollection.entity._embedded.scheduledModules.forEach((scheduledModule) => {
                const needToBeFetched = [
                    { rel: 'module', href: scheduledModule._links.module.href },
                    { rel: 'timeslot', href: scheduledModule._links.timeslot.href },
                    { rel: 'venue', href: scheduledModule._links.venue.href }
                ];
                const arrayOfPromises = needToBeFetched.map(toFetch => {
                    return client({
                        method: 'GET',
                        path: toFetch.href
                    }).then(fetched => {
                        scheduledModule[toFetch.rel] = fetched;
                    });
                });

                // FUTURE: This command blocks! There's no async here
                Promise.all(arrayOfPromises).then(() => {
                    this.setState(previousState => ({
                        scheduledModules: previousState.scheduledModules.concat(scheduledModule),
                        fetchDoneWhenZero: previousState.fetchDoneWhenZero - 1
                    }));
                });
            });
        });
    }

    refreshTimetableAfterEvent(message) {
        // TODO: This event has to fire when a Job finishes (originating at the server), and then it will re-do the fetch, overriding the version guard
        // follow(client, apiRoot, ['schedules']).then(schedulesCollection => {
        //     return schedulesCollection.entity._embedded.schedules.map(schedule => {
        //         return client({
        //             method: 'GET',
        //             path: schedule._links.self.href
        //         });
        //     });
        // }).then(schedulesPromises => {
        //     return when.all(schedulesPromises);
        // }).then(schedules => {
        //     this.setState({
        //         schedules: schedules
        //     });
        // });
    }

    componentDidUpdate() {
        this.loadFromServer();
    }

    componentDidMount() {
        this.loadFromServer();
        // When WebSockets broker sends us back these events, then perform these callback actions
        // This is how the data is re-loaded after an object is updated in the DB
        // TODO: in EventHandler.java, add topics for this component
        // stompClient.register([
        //     { route: '/topic/newSchedule', callback: this.refreshTimetableAfterEvent },
        //     { route: '/topic/updateSchedule', callback: this.refreshTimetableAfterEvent },
        //     { route: '/topic/deleteSchedule', callback: this.refreshTimetableAfterEvent }
        // ]);
    }

    setVisibleTimetableForCourse(course) {
        this.setState({
            visibleTimetable: course
        })
    }

    render() {
        // Guard against rendering the child until ALL AJAX REQUESTS RETURNED
        if (this.state.fetchDoneWhenZero !== 0) {
            return null;
        }

        console.log('RENDERING A LIST OF SCHEDULED_MODULES');
        console.log(this.state.scheduledModules);

        // Look through all the scheduled modules for this schedule,
        // and create a list of courses, where each course has every scheduled module that belongs to it
        let courses = [];
        this.state.scheduledModules.forEach((scheduledModule, index) => {
            // console.log('looking at module #' + index); // DEBUG
            // Look through each course for this scheduled module
            // console.log('this module has no. of courseModules:', scheduledModule.module.entity.courseModules.length); // DEBUG
            scheduledModule.module.entity.courseModules.forEach((courseModule, i) => {
                // console.log('looking at module #', i); // DEBUG
                if (!courses[courseModule.id.courseId]) {
                    // console.log("making a new array for courseId=", courseModule.id.courseId); // DEBUG
                    courses[courseModule.id.courseId] = [];
                }
                courses[courseModule.id.courseId].push(scheduledModule);
                // console.log("array is now:", courses[courseModule.id.courseId]); // DEBUG
            });
        });
        console.log('sorted by courses', courses);

        const courseLinks = [], courseTimetables = [];
        courses.forEach((scheduledModule, index) => {
            courseLinks.push((
                <div>course</div>
            ));
            courseTimetables.push((
                <div>week</div>
            //     <ScheduledModuleList loggedInUser={this.props.loggedInUser}
            // scheduledModules={this.state.scheduledModules} />
            ))
        });

        return (
            <Container>
                <Row>
                    {courseLinks}
                </Row>
                <Row>
                    {courseTimetables}
                </Row>
            </Container>
        );
    }
}

class ScheduledModuleList extends React.Component {
    render() {
        const modules = this.props.scheduledModules.map(scheduledModule => {
            return (
                <ScheduledModuleItem key={scheduledModule.id.moduleId}
                                     loggedInUser={this.props.loggedInUser}
                                     scheduledModule={scheduledModule} />
            );
        });

        return (
            <table>
                <thead>
                    <tr>
                        <th>Module</th>
                        <th>Venue</th>
                        <th>Timeslot</th>
                    </tr>
                </thead>
                <tbody>
                    {modules}
                </tbody>
            </table>
        );
    }
}

class ScheduledModuleItem extends React.Component {
    render() {
        const days = ['Mon', 'Tues', 'Wed', 'Thur', 'Fri'];
        return (
            <tr>
                <td>{this.props.scheduledModule.module.entity.name}</td>
                <td>{this.props.scheduledModule.venue.entity.name}</td>
                <td>{days[this.props.scheduledModule.timeslot.entity.day] + ' ' + this.props.scheduledModule.timeslot.entity.time + ':00'}</td>
            </tr>
        );
    }
}

class AvailableSchedulesTable extends React.Component {

    constructor(props) {
        super(props);
        // State is data that this component manages; can change over time
        this.state = {
            schedules: []
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

class RunGeneticAlgorithm extends React.Component {
    constructor(props) {
        super(props);
        this.handleClick = this.handleClick.bind(this);
        this.state = {
            icon: faMicrochip,
            spin: false,
            disabled: false,
            buttonText: 'Start genetic algorithm'
        };
    }

    handleClick(e) {
        e.preventDefault();
        this.setState({
            icon: faSpinner,
            spin: true,
            disabled: true,
            buttonText: 'Starting job on server'
        });

        // Can't actually use follow.js: we're making a POST request: follow(client, apiGeneticAlgorithmRoot, ['job'])
        // TODO: Once genetic-algorithm-api root has an endpoint that returns all other endpoints, we can use follow to get the endpoint, then will still use client() to make the POST. See: https://github.com/spring-guides/tut-react-and-spring-data-rest/blob/master/security/src/main/js/app.js#L81
        // Create a Genetic Algorithm Job through the GA API
        client({
            method: 'POST',
            path: apiGeneticAlgorithmRoot + '/job',
            params: { scheduleId: this.props.schedule.entity.scheduleId }
        }).done(response => {
            console.log('Job submitted, response received:', response); // DEBUG
            // TODO: Do something with this job JSON object?
            this.setState({
                icon: faMicrochip,
                spin: false,
                disabled: false,
                buttonText: 'Start genetic algorithm'
            });

            this.props.onJob(response.entity.jobId, response.entity.totalGenerations, response.entity.startDate, this.props.schedule); // Inform UP the hierarchy that a new job has been submitted
        });
    }

    render() {
        return (
            <div>
                <Button className="jobRunnerButton" ref="jobRunner"
                        onClick={this.handleClick}
                        variant="primary"
                        disabled={this.state.disabled}>
                    <FontAwesomeIcon icon={this.state.icon} spin={this.state.spin} /> {this.state.buttonText}
                </Button>
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
                <Table hover bordered>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Creator</th>
                            <th>Is New Job</th>
                            {/*TODO*/}
                            <th>Created On</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        {schedules}
                    </tbody>
                </Table>
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
                <td></td>
                <td>{creationDate.toLocaleDateString()}</td>
                <td>
                    <RunGeneticAlgorithm key={this.props.schedule.entity.scheduleId}
                                         loggedInUser={this.props.loggedInUser}
                                         onJob={this.props.onJob}
                                         schedule={this.props.schedule} />
                </td>
            </tr>
        );
    }
}

class DisplayName extends React.Component {
    constructor(props) {
        super(props);
        this.state = { displayName: '' };
    }

    loadFromServer() {
        // Get all schedules that were created by this logged-in user
        follow(client, apiRoot, [
            'users',
            'search',
            { rel: 'username', params: { username: this.props.loggedInUser } }
        ]).done(user => {
            this.setState({
                displayName: user.entity.displayName
            });
        });
    }

    componentDidMount() {
        this.loadFromServer();
    }

    render() {
        return (
            <span><FontAwesomeIcon icon={faUser} /> {this.state.displayName}</span>
        );
    }
}


class BrandIcon extends React.Component {
    render() {
        return (
            <FontAwesomeIcon icon={faDna} size="lg" />
        );
    }
}

ReactDOM.render(
    <App loggedInUser={document.getElementById('loggedInUser').innerHTML} />,
    document.getElementById('react')
);
ReactDOM.render(
    <BrandIcon />,
    document.getElementById('brand-icon-placeholder')
);
ReactDOM.render(
    <DisplayName loggedInUser={document.getElementById('loggedInUser').innerHTML} />,
    document.getElementById('user-display-name-placeholder')
);
