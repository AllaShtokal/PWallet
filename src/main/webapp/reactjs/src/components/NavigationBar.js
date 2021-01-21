import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Navbar, Nav, Card} from 'react-bootstrap';
import {Link} from 'react-router-dom';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {faUserPlus, faSignInAlt, faSignOutAlt, faEquals} from '@fortawesome/free-solid-svg-icons';
import {logoutUser} from '../services/index';

class NavigationBar extends Component {
    logout = () => {
        this.props.logoutUser();
    };

    render() {
        const guestLinks = (
            <>
                <div className="mr-auto"/>
                <Nav className="navbar-right">
                    <Link to={"register"} className="nav-link">Register</Link>
                    <Link to={"login"} className="nav-link"> Login</Link>
                </Nav>
            </>
        );
        const userLinks = (
            <>
                <Nav className="mr-auto">
                    <Link to={"/add"} className="nav-link">Add Password</Link>
                    <Link to={"/list"} className="nav-link">Password List</Link>
                    <Link to={"/actions"} className="nav-link">Function Runs</Link>
                    <Link to={"/change"} className="nav-link">Change Master Password</Link>
                </Nav>
                <Nav className="navbar-right">
                    <Link className="nav-link" ><FontAwesomeIcon  />  mode: {localStorage.getItem("mode")}</Link>
                    <Link to={"logout"} className="nav-link" onClick={this.logout}><FontAwesomeIcon icon={faSignOutAlt} /> Logout</Link>
                </Nav>
            </>
        );

        return(
            <Navbar  bg="light" variant="light">
                <Link to={""} className="navbar-brand">

                    PW app
                </Link>
                {this.props.auth.isLoggedIn === "0" ? userLinks : guestLinks}
            </Navbar>
        );
    };
};

const mapStateToProps = state => {
    return {
        auth: state.auth
    };
};

const mapDispatchToProps = dispatch => {
    return {
        logoutUser: () => dispatch(logoutUser())
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(NavigationBar);