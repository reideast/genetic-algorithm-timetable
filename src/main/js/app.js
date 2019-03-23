const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./client');

class App extends React.Component {

    constructor(props) {
        super(props);
        this.state = { schedules: [] }; // State is data that this component manages; can change over time
    }

    componentDidMount() {
        client({ method: 'GET', path: '/api/schedule' }).done(response => {
            this.setState({ schedules: response.entity._embedded.schedule });
        });
    }

    render() {
        return (
            <ScheduleList schedules={this.state.schedules}/>
        )
    }
}

class ScheduleList extends React.Component {
    render() {
        // debug: data or employee?? <Schedule key={schedule._links.self.href} data={schedule} />
        const schedules = this.props.schedules.map(schedule =>
            <Schedule key={schedule._links.self.href} schedule={schedule} />
        );
        return (
            <table>
                <tbody>
                    <tr>
                        <th>Creator</th>
                        <th>ScheduleId</th>
                        <th>CreationDate</th>
                        <th>IsMaster</th>
                        <th>ScheduledModules</th>
                    </tr>
                    {schedules}
                </tbody>
            </table>
        )
    }
}

class Schedule extends React.Component {
    render() {
        return (
            <tr>
                <td>creator</td>
                <td>{ this.props.schedule.scheduleId }</td>
                <td>{ this.props.schedule.creationDate }</td>
                <td>{ this.props.schedule.master }</td>
                <td>scheduledModules</td>
            </tr>
        )
    }
}

ReactDOM.render(
    <App />,
    document.getElementById('react')
);
