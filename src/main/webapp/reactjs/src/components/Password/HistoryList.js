import React, {Component} from 'react';
import './../../assets/css/Style.css';
import {Card, Table, Image, ButtonGroup, Button, InputGroup, FormControl} from 'react-bootstrap';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {
    faList,
    faEdit,
    faTrash,
    faStepBackward,
    faFastBackward,
    faStepForward,
    faFastForward,
    faSearch,
    faTimes,
    faFighterJet,
    faLockOpen,
    faEye,
    faShare
} from '@fortawesome/free-solid-svg-icons';
import {Link} from 'react-router-dom';
import MyToast from '../MyToast';
import axios from 'axios';
import {connect} from "react-redux";
import Modal from "react-awesome-modal";
import {showPassword} from "../../services";

class HistoryList extends Component {

    constructor(props) {
        super(props);
        this.state = {
            actions: [],
            search: '',
            currentPage: 1,
            actionsPerPage: 10,
            sortDir: "asc",
            visible: false,
            share: false,
            sharedLogin: "",
            mode: localStorage.getItem("mode"),
            isAlertModeVisible: false,
            passwordId: this.props.match.params.id
        };
    }

    sortData = () => {
        setTimeout(() => {
            this.state.sortDir === "asc" ? this.setState({sortDir: "desc"}) : this.setState({sortDir: "asc"});
            this.findAllActions(this.state.currentPage);
        }, 100);
    };

    componentDidMount() {
        this.findAllActions();
    }

    findAllActions() {

        axios.get("http://localhost:8080/api/v1/datachange/all?login=" + localStorage.getItem('login')
            + "&passwordId=" + this.state.passwordId)
            .then(response => response.data)
            .then((data) => {
                this.setState({
                    actions: data
                });
            });
    };

    restoreHistory = actionId => {


        const restore = {
            userLogin: localStorage.getItem('login'),
            actionId: actionId,
            sharedLogin: this.state.sharedLogin
        };


        axios.put("http://localhost:8080/api/v1/password/restore?changeId=" + restore.actionId +
            "&login=" + restore.userLogin)
            .then(response => {
                    if (response.data === true) {

                        this.setState({share: true});

                        setTimeout(() => this.setState({"share": false}), 100);
                        this.findAllActions();
                    } else {
                        this.setState({"share": false});
                        alert("something wrong")
                    }
                }
               );



    };

    render() {
        const {actions, currentPage, totalPages, search, sharedLogin, share, mode, isAlertModeVisible} = this.state;

        return (
            <div>

                <Card className={"border border-dark bg-dark text-white"}>
                    <Card.Header>
                        <div style={{"float": "left"}}>
                            <FontAwesomeIcon icon={faList}/> Function runs by User  {localStorage.getItem("login")}
                        </div>
                        <div style={{"float": "right"}}>
                            <InputGroup size="sm">
                                <FormControl placeholder="Search" name="search" value={search}
                                             className={"info-border bg-dark text-white"}
                                             onChange={this.searchChange}/>
                                <InputGroup.Append>
                                    <Button size="sm" variant="outline-info" type="button" onClick={this.searchData}>
                                        <FontAwesomeIcon icon={faSearch}/>
                                    </Button>
                                    <Button size="sm" variant="outline-danger" type="button"
                                            onClick={this.cancelSearch}>
                                        <FontAwesomeIcon icon={faTimes}/>
                                    </Button>
                                </InputGroup.Append>
                            </InputGroup>
                        </div>
                    </Card.Header>
                    <Card.Body>
                        <Table bordered hover striped variant="dark">
                            <thead>
                            <tr>
                                <th>ID</th>
                                <th>Previous Value Of Record</th>
                                <th>Present Value Of Record</th>
                                <th>Time</th>
                                <th>Action Type</th>
                                <th>Go to </th>
                            </tr>
                            </thead>
                            <tbody>
                            {
                                actions.length === 0 ?
                                    <tr align="center">
                                        <td colSpan="7">No function run.</td>
                                    </tr> :
                                    actions.map((action) => (
                                        <tr key={action.id}>
                                            <td>{action.id} </td>
                                            <td>{action.previousValueOfRecord} </td>
                                            <td>{action.presentValueOfRecord}</td>
                                            <td>{action.time}</td>
                                            <td>{action.actionType}</td>
                                            <td>
                                                <ButtonGroup>
                                                    <Button size="sm" variant="outline-primary"
                                                            onClick={this.restoreHistory.bind(this, action.id)}
                                                            ><FontAwesomeIcon
                                                        icon={faFighterJet}
                                                    /></Button>
                                                </ButtonGroup>
                                            </td>

                                        </tr>
                                    ))
                            }
                            </tbody>
                        </Table>
                    </Card.Body>
                    {actions.length > 0 ?
                        <Card.Footer>
                            <div style={{"float": "left"}}>
                                Showing Page {currentPage} of {totalPages}
                            </div>
                            <div style={{"float": "right"}}>
                                <InputGroup size="sm">
                                    <InputGroup.Prepend>
                                        <Button type="button" variant="outline-info" disabled={currentPage === 1}
                                                onClick={this.firstPage}>
                                            <FontAwesomeIcon icon={faFastBackward}/> First
                                        </Button>
                                        <Button type="button" variant="outline-info" disabled={currentPage === 1}
                                                onClick={this.prevPage}>
                                            <FontAwesomeIcon icon={faStepBackward}/> Prev
                                        </Button>
                                    </InputGroup.Prepend>
                                    <FormControl className={"page-num bg-dark"} name="currentPage" value={currentPage}
                                                 onChange={this.changePage}/>
                                    <InputGroup.Append>
                                        <Button type="button" variant="outline-info"
                                                disabled={currentPage === totalPages}
                                                onClick={this.nextPage}>
                                            <FontAwesomeIcon icon={faStepForward}/> Next
                                        </Button>
                                        <Button type="button" variant="outline-info"
                                                disabled={currentPage === totalPages}
                                                onClick={this.lastPage}>
                                            <FontAwesomeIcon icon={faFastForward}/> Last
                                        </Button>
                                    </InputGroup.Append>
                                </InputGroup>
                            </div>
                        </Card.Footer> : null
                    }
                </Card>
            </div>
        );
    }

}

const mapStateToProps = state => {
    return {
        actionObject: state.action
    };
};

const mapDispatchToProps = dispatch => {
    return {
        showPassword: (passwordId) => dispatch(showPassword(passwordId))
    };
};
export default connect(mapStateToProps,mapDispatchToProps)(HistoryList);