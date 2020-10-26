import React from 'react';
import './App.css';

import {Container, Row, Col} from 'react-bootstrap';
import {BrowserRouter as Router, Switch, Route} from 'react-router-dom';

import NavigationBar from './components/NavigationBar';
import Welcome from './components/Welcome';
import PasswordList from './components/Password/PasswordList';
import UserList from './components/User/UserList';
import Register from './components/User/Register';
import Login from './components/User/Login';
import Footer from './components/Footer';
import Password from "./components/Password/Password";

export default function App() {

  const heading = "Welcome to Passs Wallet";
  const quote = "A simple web application to securely manage passwords.";
  const footer = "Alla Shtokal";

  return (
    <Router>
        <NavigationBar/>
        <Container>
            <Row>
                <Col lg={12} className={"margin-top"}>
                    <Switch>
                        <Route path="/" exact component={() => <Welcome heading={heading} quote={quote} footer={footer}/>}/>
                        <Route path="/add" exact component={Password}/>
                        <Route path="/edit/:id" exact component={Password}/>
                        <Route path="/list" exact component={PasswordList}/>
                        <Route path="/users" exact component={UserList}/>
                        <Route path="/register" exact component={Register}/>
                        <Route path="/login" exact component={Login}/>
                        <Route path="/logout" exact component={Login}/>
                    </Switch>
                </Col>
            </Row>
        </Container>
        <Footer/>
    </Router>
  );
}
