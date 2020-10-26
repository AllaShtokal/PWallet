import React, {Component} from 'react';

import {connect} from 'react-redux';
import {savePassword, fetchpassword, updatepassword} from '../../services/index';

import {Card, Form, Button, Col, InputGroup, Image} from 'react-bootstrap';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {faSave, faPlusSquare, faUndo, faList, faEdit} from '@fortawesome/free-solid-svg-icons';
import MyToast from '../MyToast';
import axios from 'axios';

class Password extends Component {

    constructor(props) {
        super(props);
        this.state = this.initialState;
        this.state = {
            genres: [],
            languages : [],
            show : false
        };
        this.passwordChange = this.passwordChange.bind(this);
        this.submitpassword = this.submitpassword.bind(this);
    }

    initialState = {
        id:'', login:'', password:'', webURL:'', description:''
    };

    componentDidMount() {
        const passwordId = +this.props.match.params.id;
        if(passwordId) {
            this.findpasswordById(passwordId);
        }
        this.findAllLanguages();
        this.findAllGenres();
    }

    findAllLanguages = () => {
        axios.get("http://localhost:8081/rest/passwords/languages")
            .then(response => response.data)
            .then((data) => {
                this.setState({
                    languages: [{value:'', display:'Select Language'}]
                        .concat(data.map(language => {
                            return {value:language, display:language}
                        }))
                });
            });
    };

    findAllGenres = () => {
        axios.get("http://localhost:8081/rest/passwords/genres")
            .then(response => response.data)
            .then((data) => {
                this.setState({
                    genres: [{value:'', display:'Select Genre'}]
                        .concat(data.map(genre => {
                            return {value:genre, display:genre}
                        }))
                });
            });
    };

    /*findpasswordById = (passwordId) => {
        fetch("http://localhost:8081/rest/passwords/"+passwordId)
            .then(response => response.json())
            .then((password) => {
                if(password) {
                    this.setState({
                        id: password.id,
                        title: password.title,
                        author: password.author,
                        coverPhotoURL: password.coverPhotoURL,
                        isbnNumber: password.isbnNumber,
                        price: password.price,
                        language: password.language,
                        genre: password.genre
                    });
                }
            }).catch((error) => {
                console.error("Error - "+error);
            });
    };*/

    findpasswordById = (passwordId) => {
        this.props.fetchpassword(passwordId);
        setTimeout(() => {
            let password = this.props.passwordObject.password;
            if(password != null) {
                this.setState({
                    id: password.id,
                    login: password.login,
                    password: password.password,
                    webURL: password.webURL,
                    description: password.description
                });
            }
        }, 1000);
        /*axios.get("http://localhost:8081/rest/passwords/"+passwordId)
            .then(response => {
                if(response.data != null) {
                    this.setState({
                        id: response.data.id,
                        title: response.data.title,
                        author: response.data.author,
                        coverPhotoURL: response.data.coverPhotoURL,
                        isbnNumber: response.data.isbnNumber,
                        price: response.data.price,
                        language: response.data.language,
                        genre: response.data.genre
                    });
                }
            }).catch((error) => {
                console.error("Error - "+error);
            });*/
    };

    resetpassword = () => {
        this.setState(() => this.initialState);
    };

    /*submitpassword = event => {
        event.preventDefault();

        const password = {
            title: this.state.title,
            author: this.state.author,
            coverPhotoURL: this.state.coverPhotoURL,
            isbnNumber: this.state.isbnNumber,
            price: this.state.price,
            language: this.state.language,
            genre: this.state.genre
        };

        const headers = new Headers();
        headers.append('Content-Type', 'application/json');

        fetch("http://localhost:8081/rest/passwords", {
            method: 'POST',
            body: JSON.stringify(password),
            headers
        })
        .then(response => response.json())
        .then((password) => {
            if(password) {
                this.setState({"show":true, "method":"post"});
                setTimeout(() => this.setState({"show":false}), 3000);
            } else {
                this.setState({"show":false});
            }
        });
        this.setState(this.initialState);
    };*/

    submitpassword = event => {
        event.preventDefault();

        const password = {
            login: this.state.login,
            password: this.state.password,
            webURL: this.state.webURL,
            description: this.state.description
        };

        this.props.savePassword(password);
        setTimeout(() => {
            if(this.props.savedpasswordObject.password != null) {
                this.setState({"show":true, "method":"post"});
                setTimeout(() => this.setState({"show":false}), 3000);
            } else {
                this.setState({"show":false});
            }
        }, 2000);
        /*axios.post("http://localhost:8081/rest/passwords", password)
            .then(response => {
                if(response.data != null) {
                    this.setState({"show":true, "method":"post"});
                    setTimeout(() => this.setState({"show":false}), 3000);
                } else {
                    this.setState({"show":false});
                }
            });*/

        this.setState(this.initialState);
    };

    /*updatepassword = event => {
        event.preventDefault();

        const password = {
            id: this.state.id,
            title: this.state.title,
            author: this.state.author,
            coverPhotoURL: this.state.coverPhotoURL,
            isbnNumber: this.state.isbnNumber,
            price: this.state.price,
            language: this.state.language,
            genre: this.state.genre
        };

        const headers = new Headers();
        headers.append('Content-Type', 'application/json');

        fetch("http://localhost:8081/rest/passwords", {
            method: 'PUT',
            body: JSON.stringify(password),
            headers
        })
        .then(response => response.json())
        .then((password) => {
            if(password) {
                this.setState({"show":true, "method":"put"});
                setTimeout(() => this.setState({"show":false}), 3000);
                setTimeout(() => this.passwordList(), 3000);
            } else {
                this.setState({"show":false});
            }
        });
        this.setState(this.initialState);
    };*/

    updatepassword = event => {
        event.preventDefault();

        const password = {
            id: this.state.id,
            login: this.state.login,
            password: this.state.password,
            webURL: this.state.webURL,
            description: this.state.description
        };
        this.props.updatepassword(password);
        setTimeout(() => {
            if(this.props.updatedpasswordObject.password != null) {
                this.setState({"show":true, "method":"put"});
                setTimeout(() => this.setState({"show":false}), 3000);
            } else {
                this.setState({"show":false});
            }
        }, 2000);
        /*axios.put("http://localhost:8081/rest/passwords", password)
            .then(response => {
                if(response.data != null) {
                    this.setState({"show":true, "method":"put"});
                    setTimeout(() => this.setState({"show":false}), 3000);
                    setTimeout(() => this.passwordList(), 3000);
                } else {
                    this.setState({"show":false});
                }
            });*/
        this.setState(this.initialState);
    };

    passwordChange = event => {
        this.setState({
            [event.target.name]:event.target.value
        });
    };

    passwordList = () => {
        return this.props.history.push("/list");
    };

    render() {
        const {login, password, webURL, description} = this.state;

        return (
            <div>
                <div style={{"display":this.state.show ? "block" : "none"}}>
                    <MyToast show = {this.state.show} message = {this.state.method === "put" ? "Password Updated Successfully." : "Password Saved Successfully."} type = {"success"}/>
                </div>
                <Card className={"border border-dark bg-dark text-white"}>
                    <Card.Header>
                        <FontAwesomeIcon icon={this.state.id ? faEdit : faPlusSquare} /> {this.state.id ? "Update Password" : "Add New Password"}
                    </Card.Header>
                    <Form onReset={this.resetpassword} onSubmit={this.state.id ? this.updatepassword : this.submitpassword} id="passwordFormId">
                        <Card.Body>
                            <Form.Row>
                                <Form.Group as={Col} controlId="formGridTitle">
                                    <Form.Label>Login</Form.Label>
                                    <Form.Control required autoComplete="off"
                                        type="text" name="login"
                                        value={login} onChange={this.passwordChange}
                                        className={"bg-dark text-white"}
                                        placeholder="Enter Login" />
                                </Form.Group>
                                <Form.Group as={Col} controlId="formGridAuthor">
                                    <Form.Label>Password</Form.Label>
                                    <Form.Control required autoComplete="off"
                                        type="password" name="password"
                                        value={password} onChange={this.passwordChange}
                                        className={"bg-dark text-white"}
                                        placeholder="Enter Password " />
                                </Form.Group>
                            </Form.Row>
                            <Form.Row>
                                <Form.Group as={Col} controlId="formGridCoverPhotoURL">
                                    <Form.Label>Cover Photo URL</Form.Label>
                                    <InputGroup>
                                        <Form.Control required autoComplete="off"
                                            type="text" name="webURL"
                                            value={webURL} onChange={this.passwordChange}
                                            className={"bg-dark text-white"}
                                            placeholder="Enter Web-site URL" />
                                    </InputGroup>
                                </Form.Group>
                                <Form.Group as={Col} controlId="formGridISBNNumber">
                                    <Form.Label>Description</Form.Label>
                                    <Form.Control required autoComplete="off"
                                        type="text" name="description"
                                        value={description} onChange={this.passwordChange}
                                        className={"bg-dark text-white"}
                                        placeholder="Enter description" />
                                </Form.Group>
                            </Form.Row>

                        </Card.Body>
                        <Card.Footer style={{"textAlign":"right"}}>
                            <Button size="sm" variant="success" type="submit">
                                <FontAwesomeIcon icon={faSave} /> {this.state.id ? "Update" : "Save"}
                            </Button>{' '}
                            <Button size="sm" variant="info" type="reset">
                                <FontAwesomeIcon icon={faUndo} /> Reset
                            </Button>{' '}
                            <Button size="sm" variant="info" type="button" onClick={this.passwordList.bind()}>
                                <FontAwesomeIcon icon={faList} /> Password List
                            </Button>
                        </Card.Footer>
                    </Form>
                </Card>
            </div>
        );
    }
};

const mapStateToProps = state => {
    return {
        savedpasswordObject: state.password,
        passwordObject: state.password,
        updatedpasswordObject: state.password
    };
};

const mapDispatchToProps = dispatch => {
    return {
        savePassword: (password) => dispatch(savePassword(password)),
        fetchpassword: (passwordId) => dispatch(fetchpassword(passwordId)),
        updatepassword: (password) => dispatch(updatepassword(password))
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(Password);