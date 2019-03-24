const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./client');
const follow = require('./follow');

const apiRoot = '/api';
const apiGeneticAlgorithmRoot = '/genetic-algorithm-api';

class App extends React.Component {

    constructor(props) {
        super(props);
        this.state = { schedules: [], attributes: [], pageSize: 5, links: {} }; // State is data that this component manages; can change over time
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
                this.schema = schema.entity;
                return scheduleCollection;
            });
        }).done(scheduleCollection => {
            this.setState({
                schedules: scheduleCollection.entity._embedded.schedule,
                attributes: Object.keys(this.schema.properties),
                pageSize: pageSize,
                links: scheduleCollection.entity._links
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
                <CreateDialog attributes={this.state.attributes} onCreate={this.onCreate} />
                <ScheduleList schedules={this.state.schedules}
                              links={this.state.links}
                              pageSize={this.state.pageSize}
                              onNavigate={this.onNavigate}
                              onDelete={this.onDelete}
                              updatePageSize={this.updatePageSize} />
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
        const schedules = this.props.schedules.map(schedule =>
            <Schedule key={schedule._links.self.href} schedule={schedule} onDelete={this.props.onDelete} />
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
                {/* creator is a _link not part of the entity._embedded, so this won't work: <td>{this.props.schedule.creator}</td>*/}
                <td>creator</td>
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
