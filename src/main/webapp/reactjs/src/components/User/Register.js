import React, {Component} from 'react';
import {Row, Col, Card, Form, InputGroup, FormControl, Button} from 'react-bootstrap';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faEnvelope, faLock, faUndo, faUserPlus} from "@fortawesome/free-solid-svg-icons";
import axios from "axios";
import MyToast from "../MyToast";

export default class Register extends Component {
    constructor(props) {
        super(props);
        this.state = this.initialState;
    }

    initialState = {
        login: '', password: '', password2: '', passStoreMethod:'SHA512'
    };

    submitRegisterForm = event => {
        event.preventDefault();


        const user = {
            login: this.state.login,
            password: this.state.password,
            isPasswordSavedAsHash: this.state.passStoreMethod === 'SHA512'
        };
        axios.post("http://localhost:8080/api/v1/user/add", user)
            .then(response => {
                if (response.data != null) {
                    this.setState({"show": true});
                    setTimeout(() => this.setState({"show": false}), 3000);
                } else {
                    this.setState({"show": false});
                }
            });
        this.setState(this.initialState);
    }


    userChange = event => {
        this.setState({
            [event.target.name]: event.target.value
        });
    };

    resetRegisterForm = () => {
        this.setState(() => this.initialState);
    };


    render() {
        const {login, password, password2, passStoreMethod} = this.state;

        return (
            <div>
                <div style={{"display": this.state.show ? "block" : "none"}}>
                    <MyToast children={{show: this.state.show, message: "Registered Successfully."}}/>
                </div>
                <Row className="justify-content-md-center">
                    <Col xs={5}>
                        <Card className={"border border-dark bg-dark text-white"}>
                            <Card.Header>
                                <FontAwesomeIcon icon={faUserPlus}/> Register
                            </Card.Header>
                            <Card.Body>
                                <Form.Row>
                                    <Form.Group as={Col}>
                                        <InputGroup>
                                            <InputGroup.Prepend>
                                                <InputGroup.Text><FontAwesomeIcon icon={faEnvelope}/></InputGroup.Text>
                                            </InputGroup.Prepend>
                                            <FormControl required autoComplete="off" type="text" name="login"
                                                         value={login} onChange={this.userChange}
                                                         className={"bg-dark text-white"} placeholder="Enter Login"/>
                                        </InputGroup>
                                    </Form.Group>
                                </Form.Row>
                                <Form.Row>
                                    <Form.Group as={Col}>
                                        <InputGroup>
                                            <InputGroup.Prepend>
                                                <InputGroup.Text><FontAwesomeIcon icon={faLock}/></InputGroup.Text>
                                            </InputGroup.Prepend>
                                            <FormControl required autoComplete="off" type="password" name="password"
                                                         value={password} onChange={this.userChange}
                                                         className={"bg-dark text-white"} placeholder="Enter Password"/>
                                        </InputGroup>
                                    </Form.Group>
                                </Form.Row>
                                <Form.Row>
                                    <Form.Group as={Col}>
                                        <InputGroup>
                                            <InputGroup.Prepend>
                                                <InputGroup.Text><FontAwesomeIcon icon={faLock}/></InputGroup.Text>
                                            </InputGroup.Prepend>
                                            <FormControl required autoComplete="off" type="password" name="password2"
                                                         value={password2} onChange={this.userChange}
                                                         className={"bg-dark text-white"}
                                                         placeholder="Enter Password One More Time"/>
                                        </InputGroup>
                                    </Form.Group>
                                </Form.Row>
                                <Form.Row>
                                    <Form.Group as={Col}>
                                        <Form.Label>Choose a method to store your password</Form.Label>
                                        <Form.Control as="select"
                                                      className={"bg-dark text-white"}
                                                      onChange={this.userChange}
                                                      name={"passStoreMethod"}
                                                      value={passStoreMethod}
                                        >
                                            <option value={"SHA512"}>SHA512 with salt and pepper</option>
                                            <option value={"HMAC"}>HMAC</option>
                                        </Form.Control>
                                    </Form.Group>
                                </Form.Row>
                            </Card.Body>
                            <Card.Footer style={{"textAlign": "right"}}>
                                <Button size="sm" type="button" variant="success" onClick={this.submitRegisterForm}
                                        disabled={this.state.login.length === 0 || this.state.password.length === 0 || this.state.password !== this.state.password2}>
                                    <FontAwesomeIcon icon={faUserPlus}/> Register
                                </Button>{' '}
                                <Button size="sm" type="button" variant="info" onClick={this.resetRegisterForm}>
                                    <FontAwesomeIcon icon={faUndo}/> Reset
                                </Button>
                            </Card.Footer>
                        </Card>
                    </Col>
                </Row></div>
        );
    }
}