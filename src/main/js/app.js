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
import Tab from 'react-bootstrap/Tab';
import OverlayTrigger from 'react-bootstrap/OverlayTrigger';
import Popover from 'react-bootstrap/Popover';

const apiRoot = '/api';
const apiGeneticAlgorithmRoot = '/genetic-algorithm-api';

// FUTURE: This information should be fetched from the database, perhaps on app load
const dayNames = ['Mon', 'Tues', 'Wed', 'Thur', 'Fri'];
const days = [0, 1, 2, 3, 4];
const hours = [8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18];

// TODO: need a REST controller for the root of /genetic-algorithm-api, which returns metadata for all possible endpoints. How does spring-data do it?

class App extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <Container className="bg-white">
                <div className="py-3 pt-md-5 pb-md-4 mx-auto text-center">
                    <h4 className="display-4">Run Genetic Algorithm</h4>
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
        // SEE: https://getbootstrap.com/docs/4.3/components/progress/
        // console.log('STUB ON JOB', schedule.entity.scheduleId); // DEBUG
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
            // console.log('making timetable out of schedule:', this.state.currentTimetableSchedule[0]); // DEBUG
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
            courses: [],
            scheduledModules: [],
            fetchDoneWhenZero: -1,
            visibleTimetable: -1
        };
        this.numRowsPerCell = 2;
        this.refreshTimetableAfterEvent = this.refreshTimetableAfterEvent.bind(this);
    }

    loadFromServer() {
        if (this.state.currentDisplayedVersion === this.props.localDisplayedVersion) {
            return; // This schedule has already been fetched
        }
        this.setState({
            fetchDoneWhenZero: -1,
            currentDisplayedVersion: this.props.localDisplayedVersion,
            scheduledModules: [],
            courses: []
        });

        let courses = [];
        const coursesWithFetchStartedOrDone = {};
        follow(client, apiRoot, [
            'scheduledModules',
            'search',
            { rel: 'schedule', params: { id: this.props.schedule.entity.scheduleId } }
        ]).then(scheduledModulesCollection => {
            this.setState({
                fetchDoneWhenZero: scheduledModulesCollection.entity._embedded.scheduledModules.length + 1
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

                // Look through all the scheduled modules for this schedule,
                // and create a list of courses, where each course has every scheduled module that belongs to it
                // FUTURE: This command blocks! There's no async here
                Promise.all(arrayOfPromises).then(() => {
                    const arrayOfCoursePromises = [];
                    scheduledModule.module.entity.courseModules.forEach((courseModule) => {
                        if (!courses[courseModule.id.courseId]) {
                            courses[courseModule.id.courseId] = {
                                courseId: courseModule.id.courseId,
                                courseName: courseModule.id.courseId, // Temporarily just use ID no.
                                scheduledModules: []
                            };
                        }
                        // console.log('making a new course array entry for courseId=', courseModule.id.courseId); // DEBUG
                        if (!coursesWithFetchStartedOrDone[courseModule.id.courseId]) {
                            coursesWithFetchStartedOrDone[courseModule.id.courseId] = true;
                            arrayOfCoursePromises.push(client({
                                method: 'GET',
                                path: courseModule._links.course.href
                            }).then(course => {
                                courses[courseModule.id.courseId].courseName = course.entity.name;
                            }));
                        }
                    });
                    // Block until any possible ajax calls come back
                    Promise.all(arrayOfCoursePromises).then(() => {
                        // console.log('All courses (needed to fetch n=' + arrayOfCoursePromises.length + ')');

                        // console.log('looking at module #' + index); // DEBUG
                        // Look through each course for this scheduled module
                        // console.log('this module has no. of courseModules:', scheduledModule.module.entity.courseModules.length); // DEBUG
                        scheduledModule.module.entity.courseModules.forEach((courseModule) => {
                            // console.log('looking at module #', i); // DEBUG
                            if (!courses[courseModule.id.courseId]) {
                                console.error('Assertion error: Course with id=' + courseModule.id.courseId + ' was not fetched in the sub-ajax call above');
                            }
                            courses[courseModule.id.courseId].scheduledModules.push(scheduledModule);
                            // console.log("array is now:", courses[courseModule.id.courseId]); // DEBUG
                        });

                        // console.log('SCHEDULED MODULE', scheduledModule.id.moduleId, scheduledModule); // DEBUG
                        client({
                            method: 'GET',
                            path: scheduledModule.module.entity._links.lecturer.href
                        }).then(lecturer => {
                            // console.log('lecturer', lecturer); // DEBUG
                            scheduledModule.lecturer = lecturer;

                            this.setState(previousState => ({
                                scheduledModules: previousState.scheduledModules.concat(scheduledModule),
                                fetchDoneWhenZero: previousState.fetchDoneWhenZero - 1
                            }));
                            // console.log('LECTURER FILLED IN MODULE', scheduledModule.id.moduleId, scheduledModule); // DEBUG
                        });
                    });
                });
            });
            return scheduledModulesCollection;
        }).done(scheduledModulesCollection => {
            this.setState(previousState => ({
                courses: courses,
                fetchDoneWhenZero: previousState.fetchDoneWhenZero - 1
            }));
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

    // DEBUG: this is now being done by a Bootstrap Tab group
    setVisibleTimetableForCourse(course) {
        this.setState({
            visibleTimetable: course
        });
    }

    render() {
        // Guard against rendering the child until ALL AJAX REQUESTS RETURNED
        if (this.state.fetchDoneWhenZero !== 0) {
            return null;
        }

        // console.log('RENDERING A LIST OF SCHEDULED_MODULES', this.state.scheduledModules); // DEBUG
        console.log('Rendering a list of scheduled modules, put in buckets by course ID', this.state.courses); // DEBUG

        // This tab layout can be moved to the side: Custom Tab Layout: https://react-bootstrap.github.io/components/tabs/#tabs-custom-layout
        // This may be necessary when there are a lot of courses
        const courseTabs = this.state.courses.map((course) => {
            return (
                <Tab eventKey={course.courseId}
                     title={course.courseName}
                     key={course.courseId}
                     className="pb-3">
                    <WeekView key={course.courseId}
                              loggedInUser={this.props.loggedInUser}
                              courseId={course.courseId}
                              courseName={course.courseName}
                              modules={course.scheduledModules}
                              dayNames={dayNames}
                              days={days}
                              hours={hours}
                              numRowsPerCell={this.numRowsPerCell}
                    />
                </Tab>
            );
        });

        return (
            <Tabs variant="pills"
                  transition={false}>
                {courseTabs}
            </Tabs>
        );
    }
}

class WeekView extends React.Component {
    render() {
        // Make row data structures, one for each hour
        const rows = [];
        this.props.hours.forEach(hourNum => {
            rows[hourNum] = {};
        });

        // Fill that in with modules for that hour
        this.props.modules.forEach(module => {
            rows[module.timeslot.entity.time][module.timeslot.entity.day] = module;
        });
        // console.log('rows', rows);

        // Make the actual JSX elements for those rows
        const hourRows = rows.map((row, hourNum) => {
            return (
                <WeekRow key={hourNum}
                         hour={hourNum}
                         courseId={this.props.courseId}
                         modulesByDays={row}
                         days={this.props.days} />
            );
        });

        // TODO: make the header row BOLD
        // FUTURE: dayNames.reduce --> row of headers COULD be its own component, s.t. it's only done once. Wait...would react only make one copy of it?? Research first
        return (
            <Container fluid={true} className="timetableContainer">
                <Row key={'headerRow'} className="timetableHeaderRow">
                    {/*{dayNames.reduce((day => (*/}
                    {/*<Col key={day}>{day}</Col>*/}
                    {/*)), (*/}
                    {/*<Col key={'timeColumn'}>Time</Col>*/}
                    {/*))}*/}
                    <Col xs={1} key={'times'} className="timetableHeaderTimeCell"><span className="d-none d-md-inline">Time</span></Col>
                    {
                        dayNames.map(dayName => (
                            <Col key={dayName}>
                                <span className="d-none d-md-inline">{dayName}</span>
                                <span className="d-md-none">{dayName.charAt(0)}</span>
                            </Col>
                        ))
                    }
                    {/*<Col key={'Mon'}>Mon</Col>*/}
                    {/*<Col key={'Tues'}>Tues</Col>*/}
                    {/*<Col key={'Wed'}>Wed</Col>*/}
                    {/*<Col key={'Thur'}>Thur</Col>*/}
                    {/*<Col key={'Fri'}>Fri</Col>*/}
                </Row>
                {hourRows}
            </Container>
        );
    }
}

class WeekRow extends React.Component {
    render() {
        // console.log('rendering hour=' + this.props.hour, this.props.modulesByDays, this.props.days); // DEBUG
        const timeName = this.props.hour + ':00';
        const dayColumns = this.props.days.map(day => {
            if (this.props.modulesByDays[day]) {
                return (
                    <Col key={day} className="timetableCell scheduledCell border border-primary">
                        <TimetableCell key={day}
                                       courseId={this.props.courseId}
                                       dayName={dayNames[day]}
                                       timeName={timeName}
                                       module={this.props.modulesByDays[day]} />
                    </Col>
                );
            } else {
                return (
                    <Col key={day} className="timetableCell border border-primary h-100">
                        <div></div>
                    </Col>
                );
            }
        });

        return (
            <Row>
                <Col xs={1} className="text-right my-auto pl-0 ml-0">
                    <span className="d-none d-md-inline">{timeName}</span>
                    <span className="d-md-none">{this.props.hour}</span>
                </Col>
                {dayColumns}
            </Row>
        );
    }
}

class TimetableCell extends React.Component {
    render() {
        const scheduledModule = this.props.module;
        // console.log("A cells's scheduled module: ", scheduledModule);
        const courseCodeFromCourseModule = scheduledModule.module.entity.courseModules.find(courseModule =>
            courseModule.id.courseId === this.props.courseId
        ).code;
        const lecturerName = scheduledModule.lecturer.entity.name;
        return (
            <OverlayTrigger placement="top"
                            overlay={(
                                <Popover title={`${courseCodeFromCourseModule} ${scheduledModule.module.entity.name}`}>
                                    {scheduledModule.venue.entity.name}<br />
                                    ({lecturerName})<br />
                                    {`${this.props.dayName} ${this.props.timeName}`}
                                </Popover>
                            )}>
                <div>
                    <span className="align-middle">
                        {courseCodeFromCourseModule}<br />
                        <small>
                            {scheduledModule.venue.entity.name}
                        </small>
                    </span>
                </div>
            </OverlayTrigger>
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
        // TODO: Make this table into Cards (perhaps) instead
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
