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
import {faDna, faMicrochip, faSpinner, faUser} from '@fortawesome/free-solid-svg-icons';

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
import ProgressBar from 'react-bootstrap/ProgressBar';
import Alert from 'react-bootstrap/Alert';
import CardDeck from 'react-bootstrap/CardDeck';
import Card from 'react-bootstrap/Card';
import ListGroup from 'react-bootstrap/ListGroup';
import ListGroupItem from 'react-bootstrap/ListGroupItem';


const apiRoot = '/api';
const apiGeneticAlgorithmRoot = '/genetic-algorithm-api';

// FUTURE: This information should be fetched from the database, perhaps on app load
const dayNames = ['Mon', 'Tues', 'Wed', 'Thur', 'Fri'];
const days = [0, 1, 2, 3, 4];
const hours = [9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19];

// TODO: need a REST controller for the root of /genetic-algorithm-api, which returns metadata for all possible endpoints. How does spring-data do it?

class App extends React.Component {
    constructor(props) {
        super(props);


    }

    render() {
        return (
            <Container className="bg-white">
                <Row>
                    <Col className="py-3 pt-md-5 pb-md-4 mx-auto text-center">
                        <h4 className="display-4 d-md-block d-none">Run Genetic Algorithm</h4>
                        <h1 className="d-md-none d-sm-block d-none" style={{ fontWeight: 300, lineHeight: 1.2 }}>Run Genetic Algorithm</h1>
                        <h2 className="d-sm-none d-block" style={{ fontWeight: 300, lineHeight: 1.2 }}>Run Genetic Algorithm</h2>
                    </Col>
                </Row>
                <Row>
                    <SchedulingJobLauncher loggedInUser={this.props.loggedInUser} />
                </Row>
            </Container>
        );
    }
}

class SchedulingJobLauncher extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            currentTimetableScheduleId: -1,
            currentTimetableJobId: undefined,
            alertIsShown: false,
            alertVariant: 'info',
            alertTitle: 'Alert',
            alertBody: 'Error message'
        };
        this.onJob = this.onJob.bind(this);
        this.onErrorAlert = this.onErrorAlert.bind(this);
    }

    /**
     * After a job has been started, this signal will be received
     * Update this Component's state, so that the {@link Timetable} will begin updating
     * @param scheduleId
     */
    onJob(jobId, totalGenerations, startDateString, scheduleId) {
        // console.log('STUB ON JOB', 'scheduleId=', scheduleId, 'jobId=', jobId); // DEBUG
        this.setState(previousState => ({
            currentTimetableJobId: jobId,
            currentTimetableScheduleId: scheduleId
        }));
    }

    onErrorAlert(body, title, variant = 'info') {
        this.setState({
            alertIsShown: true,
            alertTitle: title,
            alertBody: body,
            alertVariant: variant
        });
    }

    // FUTURE: make an onShowSchedule and have AvailableSchedulesTable be able to call it to show different ones (without having to start a new job)

    render() {
        return (
            <Col>
                <Row>
                    <Col>
                        <AvailableSchedulesTable loggedInUser={this.props.loggedInUser}
                                                 onJob={this.onJob} />
                    </Col>
                </Row>
                <Row>
                    <Col>
                        <JobInProgressBar currentTimetableJobId={this.state.currentTimetableJobId} />
                    </Col>
                </Row>
                <Row>
                    <Col>
                        <Alert dismissible onClose={() => {
                            this.setState({ alertIsShown: false });
                        }} show={this.state.alertIsShown} variant={this.state.alertVariant}>
                            <Alert.Heading>{this.state.alertTitle}</Alert.Heading>
                            <p>{this.state.alertBody}</p>
                        </Alert>
                    </Col>
                </Row>
                <Row className="mb-3">
                    <Col>
                        <Timetable loggedInUser={this.props.loggedInUser}
                                   onErrorAlert={this.onErrorAlert}
                                   scheduleId={this.state.currentTimetableScheduleId} />
                    </Col>
                </Row>
            </Col>
        );
    }
}

class Timetable extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            currentScheduleId: -1,
            currentFetchVersionDisplayed: undefined,
            fetchVersionDesired: undefined,
            fetchVersionInProgress: undefined,
            isLoading: false,
            fetchEnqueued: false,
            hasNewScheduleLoaded: false,
            courses: undefined,
            // scheduledModules: undefined,
            fetchDoneWhenZero: -1,
            visibleTimetable: -1
        };
        this.refreshTimetableAfterWebSocket = this.refreshTimetableAfterWebSocket.bind(this);
    }

    loadFromServer() {
        if (this.props.scheduleId < 0) {
            // console.log('didUpdate: No scheduleId, not loading'); // DEBUG
            return; // No schedule has been provided to this component yet
        }
        if (this.props.scheduleId !== this.state.currentScheduleId) {
            // console.log('didUpdate: New schedule! Starting versions over from 0'); // DEBUG
            // This is a different schedule to get from the server! Discard old fetch versions
            this.setState({
                fetchVersionInProgress: 0, // This current fetch action is what will become version 0
                currentScheduleId: this.props.scheduleId,
                currentFetchVersionDisplayed: -1,
                fetchVersionDesired: 0,
                hasNewScheduleLoaded: false
            });
            // And now, start the fetch of this new version
        } else if (this.state.fetchVersionDesired === this.state.fetchVersionInProgress) {
            // console.log('didUpdate: desired version already being fetched', this.state.fetchVersionDesired, this.state.fetchVersionInProgress); // DEBUG
            // A fetch of the desired new version has already begun. Don't start again
            return;
        } else if (this.state.currentFetchVersionDisplayed === this.state.fetchVersionDesired) {
            // console.log('didUpdate: desired version has already been fetched&displayed', this.state.fetchVersionDesired, this.state.currentFetchVersionDisplayed);
            // The version desired has already been displayed. Don't fetch again
            return;
        } else {
            // This is a new fetch job to get a newly desired version. Tag which version we're getting
            // console.log('didUpdate: starting a new version fetch job. fetch version in progress := desired version', this.state.fetchVersionDesired);

            if (this.state.isLoading) {
                // A job has AJAX requests still outstanding! Hold onto which job we'll get next, but don't do it yet
                if (!this.state.fetchEnqueued) {
                    // Only set enqueued if it hasn't already been set (prevents infinite setState<-->update loop
                    this.setState({
                        fetchEnqueued: true
                    });
                }
                return; // Don't execute yet
            } else {
                // No jobs in progress. Set state and execute immediately
                this.setState({
                    fetchVersionInProgress: this.state.fetchVersionDesired
                });
            }
        }

        // } else if (this.state.fetchVersionInProgress <= this.state.currentFetchVersionDisplayed) {
        //     // The displayed version has not yet been updated; the version we're attempting to get is a newer version
        //     return; // Don't start a new fetch, one to get a newer version is still in progress
        // }

        // if (this.state.currentFetchVersionDisplayed === this.props.localDisplayedVersion) {
        //     return; // This schedule has already been fetched
        // }

        this.setState({
            fetchDoneWhenZero: -1,
            isLoading: true
            // scheduledModules: [],
            // courses: []
        });

        let courses = [];
        const coursesWithFetchStartedOrDone = {};
        follow(client, apiRoot, [
            'scheduledModules',
            'search',
            { rel: 'schedule', params: { id: this.props.scheduleId } }
        ]).then(scheduledModulesCollection => {
            if (scheduledModulesCollection.entity._embedded.scheduledModules.length === 0) {
                // No scheduled modules exist for this schedule. It's probably a new schedule
                // Don't render the table, but need to keep this.state.currentScheduleId correct so a newer version can be fetched
                this.setState(previousState => ({
                    hasNewScheduleLoaded: false, // This is most important: it keeps the schedule from rendering
                    currentFetchVersionDisplayed: previousState.currentFetchVersionDisplayed + 1, // And this makes sure that when an update arrives, it will know that it's a new version, therefore should start a fetch
                    fetchVersionInProgress: undefined,
                    isLoading: false
                }));
            }
            this.setState({
                fetchDoneWhenZero: scheduledModulesCollection.entity._embedded.scheduledModules.length
            });
            console.log('NEW: ajax requests:', this.state.fetchDoneWhenZero);
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

                            if (this.state.fetchDoneWhenZero === 1) {
                                // This is the last pending ajax request!
                                // In this state update, swap courses to the new version. (Hey, this is like graphics double buffering! ^_^
                                this.setState(previousState => ({
                                    courses: courses,
                                    fetchVersionInProgress: undefined,
                                    isLoading: false,
                                    fetchDoneWhenZero: 0, // Cheaper to just say one, rather than doing 1 - 1 = 0....
                                    hasNewScheduleLoaded: true,
                                    currentFetchVersionDisplayed: previousState.currentFetchVersionDisplayed + 1
                                }));
                                console.log('Open ajax requests:', this.state.fetchDoneWhenZero);
                                if (this.state.fetchEnqueued) {
                                    // Since this ajax request started, a request for a new version of the timetable has come in. Trigger that enqueued job (via state update)!
                                    this.setState({
                                        fetchEnqueue: false
                                    });
                                }
                            } else {
                                // This was not the last pending request
                                this.setState(previousState => ({
                                    fetchDoneWhenZero: previousState.fetchDoneWhenZero - 1
                                }));
                                console.log('Open ajax requests:', this.state.fetchDoneWhenZero);
                            }
                        });
                    });
                });
            });
            // No more Promise chain after this, don't return anything: return scheduledModulesCollection;
        });
    }

    // This event fires hen a Job finishes (originating from the server via a WebSocket), and then it will re-do the fetch, overriding the version guard
    refreshTimetableAfterWebSocket(message) {
        const body = JSON.parse(message.body);
        console.log('GA Job done signal received as WebSocket, scheduleId=', body.scheduleId, 'valid?', body.foundValidSolution);
        if (!body.foundValidSolution) {
            console.warn('The completed job was not able to find a timetable without scheduling conflicts within ' + body.finalGenerationNumber + ' generations!');
            this.props.onErrorAlert('The completed job was not able to find a timetable without scheduling conflicts within ' + body.finalGenerationNumber + ' generations!', 'No Valid Schedule Found', 'warning');
        }
        if (this.state.currentScheduleId === body.scheduleId) {
            // 1. Set state, s.t. an update is triggered
            this.setState(previousState => ({
                fetchVersionDesired: previousState.fetchVersionDesired + 1
            }));
            // Don't call loadFromServer  manually! .setState will end up calling componentDidUpdate-->which calls-->loadFromServer()
        }
        // else, this component has changed to a new schedule in the meantime, so should NOT refresh it!
    }

    componentDidUpdate() {
        this.loadFromServer();
    }

    componentDidMount() {
        this.loadFromServer();

        // When WebSockets broker sends us back these events, then perform these callback actions
        // This is how the data is re-loaded after an object is updated in the DB
        stompClient.register([
            { route: '/topic/jobComplete', callback: this.refreshTimetableAfterWebSocket }
        ]);
    }

    render() {
        // Guard against rendering while the FIRST fetch job after switching to a new scheduleId (or the first scheduleId ever)
        if (!this.state.hasNewScheduleLoaded) {
            // It is appropriate to clear away the DOM elements until the fetch has finished
            return null;
        }

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
                              hours={hours} />
                </Tab>
            );
        });

        return (
            <Container>
                <Row>
                    <Col style={this.state.isLoading ? { opacity: 0.3 } : {}}>
                        <Tabs variant="pills"
                              transition={false}
                              className="pt-2">
                            {courseTabs}
                        </Tabs>
                    </Col>
                    <div style={this.state.isLoading ? { display: 'block' } : { display: 'none' }}>
                        <div className="fade-out-mask"></div>
                        <div className="float-spinner-center spinner-border text-primary" role="status">
                            <span className="sr-only">Loading...</span>
                        </div>
                    </div>
                </Row>
            </Container>
        );
    }
}

class JobInProgressBar extends React.Component {
    constructor(props) {
        super(props);
        console.log('= Setting up new JobInProgressBar, jobId=', this.props.currentTimetableJobId);
        this.state = {
            isVisible: false,
            progressOutOfHundred: 0
        };
        this.refreshProgressBarAfterWebSocket = this.refreshProgressBarAfterWebSocket.bind(this);
    }

    componentDidMount() {
        stompClient.register([
            { route: '/topic/jobStatus', callback: this.refreshProgressBarAfterWebSocket }
        ]);
    }

    refreshProgressBarAfterWebSocket(message) {
        const body = JSON.parse(message.body);
        // console.log('STOMP MESSAGE RECEIVED', 'json of payload', body); // DEBUG

        // Filter: only act on WebSocket messages that are for THIS Job
        if (this.props.currentTimetableJobId === body.jobId) {
            if (body.isDone) {
                this.setState({
                    isVisible: false
                });
            } else {
                this.setState({
                    isVisible: true,
                    progressOutOfHundred: body.progressPercent * 100
                });
            }
        }
    }

    render() {
        return (
            <ProgressBar now={this.state.progressOutOfHundred}
                         label={`GA Job #${this.props.currentTimetableJobId}`}
                         style={{ display: (this.state.isVisible) ? 'flex' : 'none' }}
                         className="mb-3" />
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

        // FUTURE: dayNames.reduce --> row of headers COULD be its own component, s.t. it's only done once. Wait...would react only make one copy of it?? Research first
        // FUTURE: Or, just make it global constant
        return (
            <Container fluid={true} className="timetableContainer">
                <Row key={'headerRow'} className="timetableHeaderRow no-gutters">
                    {/*{dayNames.reduce((day => (*/}
                    {/*<Col key={day}>{day}</Col>*/}
                    {/*)), (*/}
                    {/*<Col key={'timeColumn'}>Time</Col>*/}
                    {/*))}*/}
                    <Col xs={1} key={'times'} className="pr-2" className="timetableHeaderTimeCell"><span className="d-none d-md-inline">Time</span></Col>
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
                    <Col key={day} className="timetableCell scheduledCell border border-primary text-center">
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
            <Row className="no-gutters">
                <Col xs={1} className="text-right my-auto pr-2">
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
                <span>
                    {courseCodeFromCourseModule}<br />
                    <small>
                        {scheduledModule.venue.entity.name}
                    </small>
                </span>
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
        console.log('WebSocket received - updating schedule list'); // DEBUG
        follow(client, apiRoot, [
            'schedules',
            'search',
            { rel: 'creatorUsername', params: { username: this.props.loggedInUser } }
        ]).then(schedulesCollection => {
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
        // DEBUG: note that the parent HTML object is <Row>
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
            params: {
                scheduleId: this.props.schedule.entity.scheduleId
                // numGenerations: 10000,
                // numGenerations: 100000,
                // mutatePercentage: 5,
                // populationSize: 60
                // proportionRunDownGenerations: 10
            }
        }).then(response => {
            console.log('Job submitted, response received:', response); // DEBUG
            // TODO: Do something with this job JSON object?
            this.setState({
                icon: faMicrochip,
                spin: false,
                disabled: false,
                buttonText: 'Start genetic algorithm'
            });

            // Inform UP the hierarchy that a new job has been submitted
            this.props.onJob(response.entity.jobId, response.entity.totalGenerations, response.entity.startDate, this.props.schedule.entity.scheduleId);
        }, error => {
            if (error.status.code === 409) {
                // FUTURE: Make a more elegant and informative in the UI!
                // FUTURE: More elegant error handling. Toast at top of page? Popover on Launch Job button?
                alert('A Genetic Algorithm job for schedule ID #' + this.props.schedule.entity.scheduleId + ' has already been started on the server!');

                this.setState({
                    icon: faMicrochip,
                    spin: false,
                    disabled: false,
                    buttonText: 'Start genetic algorithm'
                });
            }
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
        // Sorting with a map for: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Array/sort#Sorting_with_map
        // Done this way since I'm not sure if React will react badly if a Prop is modified (although Mozilla mentions this method for efficiency)
        const dateMap = this.props.schedules.map((schedule, i) => {
            return { index: i, date: schedule.entity.creationDate };
        });
        dateMap.sort((a, b) => {
            // Sort DESC
            if (a.date > b.date) {
                return -1;
            } else if (a.date < b.date) {
                return 1;
            } else {
                return 0;
            }
        });
        const schedules = dateMap.map(item => {
            const schedule = this.props.schedules[item.index];
            return (
                <Schedule key={schedule.entity.scheduleId}
                          loggedInUser={this.props.loggedInUser}
                          schedule={schedule}
                          displayedScheduleId={this.props.displayedScheduleId}
                          onJob={this.props.onJob} />
            );
        });

        return (
            <Container>
                <Row className="mb-3">
                    {schedules}
                </Row>
            </Container>
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

        const isNew = !this.props.schedule.entity.wip; // Is not a work-in-progress, so it's new

        console.log('a schedule:', this.props.schedule, 'their creator', this.state.creator);

        return (
            <Col xs={12} sm={6} lg={4}>
                <Card className={`${this.props.displayedScheduleId === this.props.schedule.entity.scheduleId ? 'shadow-selected-card' : ''} text-center mb-4 box-shadow`}>
                    <Card.Header className={`${isNew ? 'bg-success' : 'bg-primary'} text-white font-weight-normal my-0`} as="h4">
                        {isNew ? 'New Schedule' : 'Existing Schedule'}
                    </Card.Header>
                    <Card.Body>
                        <Card.Title as="h3">
                            Schedule ID {this.props.schedule.entity.scheduleId}
                        </Card.Title>
                    </Card.Body>
                    <ListGroup>
                        <ListGroupItem>Creator: {this.state.creator.entity ? this.state.creator.entity.displayName : ''}</ListGroupItem>
                        <ListGroupItem>Created: {creationDate.toLocaleDateString()}</ListGroupItem>
                        <ListGroupItem>
                            <h3>Fitness</h3>
                            <Row>
                                <Col xs={{ span: 6, offset: 3 }} sm={{ span: 12, offset: 0 }} md={{ span: 8, offset: 2 }} lg={{ span: 8, offset: 2 }}>
                                    <div className="rounded-circle w-100 h-100 position-relative text-center text-middle bg-dark text-white"
                                         style={{ paddingTop: '100%' }}
                                    >
                                        <div style={{ position: 'absolute', top: 0, right: 0, bottom: 0, left: 0 }}>
                                            <Row className="h-100">
                                                <Col className="align-self-center">
                                                    <h4>{this.props.schedule.entity.fitness ? this.props.schedule.entity.fitness : "n/a"}</h4>
                                                </Col>
                                            </Row>
                                        </div>
                                    </div>
                                </Col>
                            </Row>
                        </ListGroupItem>
                    </ListGroup>
                    <Card.Footer>
                        {/*<DisplayTimetable key={this.props.schedule.entity.scheduleId}*/}
                        {/*                  loggedInUser={this.props.loggedInUser}*/}
                        {/*                  onJob={this.props.onJob}*/}
                        {/*                  schedule={this.props.schedule} />*/}
                        <RunGeneticAlgorithm key={this.props.schedule.entity.scheduleId}
                                             loggedInUser={this.props.loggedInUser}
                                             onJob={this.props.onJob}
                                             schedule={this.props.schedule} />
                    </Card.Footer>
                </Card>
            </Col>
        );
    }
}

class DisplayTimetable extends React.Component {
    constructor(props) {
        super(props);
        this.handleClick = this.handleClick.bind(this);
        this.icon = faMicrochip;
        this.buttonText = 'Display timetable';
        this.state = {
            buttonText: this.buttonText,
            icon: this.icon,
            spin: false,
            disabled: false
        };
    }

    handleClick(e) {
        e.preventDefault();
        this.setState({
            buttonText: 'Loading',
            icon: faSpinner,
            spin: true,
            disabled: true
        });

        alert('showingSched');
        this.setState({
            buttonText: this.buttonText,
            icon: this.icon,
            spin: false,
            disabled: false
        });

        // Create a Genetic Algorithm Job through the GA API
        // client({
        //     method: 'POST',
        //     path: apiGeneticAlgorithmRoot + '/job',
        //     params: {
        //         scheduleId: this.props.schedule.entity.scheduleId
        //         // numGenerations: 10000,
        //         // numGenerations: 100000,
        //         // mutatePercentage: 5,
        //         // populationSize: 60
        //         // proportionRunDownGenerations: 10
        //     }
        // }).then(response => {
        //     console.log('Job submitted, response received:', response); // DEBUG
        //     // TODO: Do something with this job JSON object?
        //     this.setState({
        //         icon: faMicrochip,
        //         spin: false,
        //         disabled: false,
        //         buttonText: 'Start genetic algorithm'
        //     });
        //
        //     // Inform UP the hierarchy that a new job has been submitted
        //     this.props.onJob(response.entity.jobId, response.entity.totalGenerations, response.entity.startDate, this.props.schedule.entity.scheduleId);
        // }, error => {
        //     if (error.status.code === 409) {
        //         // FUTURE: Make a more elegant and informative in the UI!
        //         // FUTURE: More elegant error handling. Toast at top of page? Popover on Launch Job button?
        //         alert('A Genetic Algorithm job for schedule ID #' + this.props.schedule.entity.scheduleId + ' has already been started on the server!');
        //
        //         this.setState({
        //             icon: faMicrochip,
        //             spin: false,
        //             disabled: false,
        //             buttonText: 'Start genetic algorithm'
        //         });
        //     }
        // });
    }

    render() {
        return (
            <Button
                onClick={this.handleClick}
                variant="primary"
                disabled={this.state.disabled}>
                <FontAwesomeIcon icon={this.state.icon} spin={this.state.spin} /> {this.state.buttonText}
            </Button>
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
