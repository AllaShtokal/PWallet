import React, {Component} from 'react';

import {connect} from 'react-redux';
import {deletePassword} from '../../services/index';

import './../../assets/css/Style.css';
import {Card, Table, Image, ButtonGroup, Button, InputGroup, FormControl} from 'react-bootstrap';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {faList, faEdit, faTrash, faStepBackward, faFastBackward, faStepForward, faFastForward, faSearch, faTimes} from '@fortawesome/free-solid-svg-icons';
import {Link} from 'react-router-dom';
import MyToast from '../MyToast';
import axios from 'axios';

class PasswordList extends Component {

    constructor(props) {
        super(props);
        this.state = {
            passwords : [],
            search : '',
            currentPage : 1,
            passwordsPerPage : 5,
            sortDir: "asc"
        };
    }

    sortData = () => {
        setTimeout(() => {
            this.state.sortDir === "asc" ? this.setState({sortDir: "desc"}) : this.setState({sortDir: "asc"});
            this.findAllPasswords(this.state.currentPage);
        }, 500);
    };

    componentDidMount() {
        this.findAllPasswords(this.state.currentPage);
    }

    /*findAllBooks() {
        fetch("http://localhost:8081/rest/books")
            .then(response => response.json())
            .then((data) => {
                this.setState({books: data});
            });
    };*/

    findAllPasswords(currentPage) {
        currentPage -= 1;
        axios.get("http://localhost:8081/rest/passwords?pageNumber="+currentPage+"&pageSize="+this.state.passwordsPerPage+"&sortBy=price&sortDir="+this.state.sortDir)
            .then(response => response.data)
            .then((data) => {
                this.setState({
                    passwords: data.content,
                    totalPages: data.totalPages,
                    totalElements: data.totalElements,
                    currentPage: data.number + 1
                });
            });
    };

    /*deleteBook = (bookId) => {
        fetch("http://localhost:8081/rest/books/"+bookId, {
            method: 'DELETE'
        })
        .then(response => response.json())
        .then((book) => {
            if(book) {
                this.setState({"show":true});
                setTimeout(() => this.setState({"show":false}), 3000);
                this.setState({
                    books: this.state.books.filter(book => book.id !== bookId)
                });
            } else {
                this.setState({"show":false});
            }
        });
    };*/

    deletePassword = (passwordId) => {
        this.props.deletePassword(passwordId);
        setTimeout(() => {
            if(this.props.passwordObject != null) {
                this.setState({"show":true});
                setTimeout(() => this.setState({"show":false}), 3000);
                this.findAllPasswords(this.state.currentPage);
            } else {
                this.setState({"show":false});
            }
        }, 1000);
        /*axios.delete("http://localhost:8081/rest/passwords/"+bookId)
            .then(response => {
                if(response.data != null) {
                    this.setState({"show":true});
                    setTimeout(() => this.setState({"show":false}), 3000);
                    this.setState({
                        books: this.state.books.filter(book => book.id !== bookId)
                    });
                } else {
                    this.setState({"show":false});
                }
            });*/
    };

    changePage = event => {
        let targetPage = parseInt(event.target.value);
        if(this.state.search) {
            this.searchData(targetPage);
        } else {
            this.findAllPasswords(targetPage);
        }
        this.setState({
            [event.target.name]: targetPage
        });
    };

    firstPage = () => {
        let firstPage = 1;
        if(this.state.currentPage > firstPage) {
            if(this.state.search) {
                this.searchData(firstPage);
            } else {
                this.findAllPasswords(firstPage);
            }
        }
    };

    prevPage = () => {
        let prevPage = 1;
        if(this.state.currentPage > prevPage) {
            if(this.state.search) {
                this.searchData(this.state.currentPage - prevPage);
            } else {
                this.findAllPasswords(this.state.currentPage - prevPage);
            }
        }
    };

    lastPage = () => {
        let condition = Math.ceil(this.state.totalElements / this.state.passwordsPerPage);
        if(this.state.currentPage < condition) {
            if(this.state.search) {
                this.searchData(condition);
            } else {
                this.findAllPasswords(condition);
            }
        }
    };

    nextPage = () => {
        if(this.state.currentPage < Math.ceil(this.state.totalElements / this.state.passwordsPerPage)) {
            if(this.state.search) {
                this.searchData(this.state.currentPage + 1);
            } else {
                this.findAllPasswords(this.state.currentPage + 1);
            }
        }
    };

    searchChange = event => {
        this.setState({
            [event.target.name] : event.target.value
        });
    };

    cancelSearch = () => {
        this.setState({"search" : ''});
        this.findAllPasswords(this.state.currentPage);
    };

    searchData = (currentPage) => {
        currentPage -= 1;
        axios.get("http://localhost:8081/rest/passwords/search/"+this.state.search+"?page="+currentPage+"&size="+this.state.passwordsPerPage)
            .then(response => response.data)
            .then((data) => {
                this.setState({
                    passwords: data.content,
                    totalPages: data.totalPages,
                    totalElements: data.totalElements,
                    currentPage: data.number + 1
                });
            });
    };

    render() {
        const {passwords, currentPage, totalPages, search} = this.state;

        return (
            <div>
                <div style={{"display":this.state.show ? "block" : "none"}}>
                    <MyToast show = {this.state.show} message = {"password Deleted Successfully."} type = {"danger"}/>
                </div>
                <Card className={"border border-dark bg-dark text-white"}>
                    <Card.Header>
                        <div style={{"float":"left"}}>
                            <FontAwesomeIcon icon={faList} /> Password List
                        </div>
                        <div style={{"float":"right"}}>
                            <InputGroup size="sm">
                                <FormControl placeholder="Search" name="search" value={search}
                                             className={"info-border bg-dark text-white"}
                                             onChange={this.searchChange}/>
                                <InputGroup.Append>
                                    <Button size="sm" variant="outline-info" type="button" onClick={this.searchData}>
                                        <FontAwesomeIcon icon={faSearch}/>
                                    </Button>
                                    <Button size="sm" variant="outline-danger" type="button" onClick={this.cancelSearch}>
                                        <FontAwesomeIcon icon={faTimes} />
                                    </Button>
                                </InputGroup.Append>
                            </InputGroup>
                        </div>
                    </Card.Header>
                    <Card.Body>
                        <Table bordered hover striped variant="dark">
                            <thead>
                            <tr>
                                <th>Login</th>
                                <th>Password</th>
                                <th>Web-site URL</th>
                                <th>Description</th>
                                <th>Actions</th>
                            </tr>
                            </thead>
                            <tbody>
                            {
                                passwords.length === 0 ?
                                    <tr align="center">
                                        <td colSpan="7">No passwords Available.</td>
                                    </tr> :
                                    passwords.map((password) => (
                                        <tr key={password.id}>

                                            <td>{password.login}</td>
                                            <td>{password.password}</td>
                                            <td>{password.webUrl}</td>
                                            <td>{password.description}</td>

                                            <td>
                                                <ButtonGroup>
                                                    <Link to={"edit/"+password.id} className="btn btn-sm btn-outline-primary"><FontAwesomeIcon icon={faEdit} /></Link>{' '}
                                                    <Button size="sm" variant="outline-danger" onClick={this.deletePassword().bind(this, password.id)}><FontAwesomeIcon icon={faTrash} /></Button>
                                                </ButtonGroup>
                                            </td>
                                        </tr>
                                    ))
                            }
                            </tbody>
                        </Table>
                    </Card.Body>
                    {passwords.length > 0 ?
                        <Card.Footer>
                            <div style={{"float":"left"}}>
                                Showing Page {currentPage} of {totalPages}
                            </div>
                            <div style={{"float":"right"}}>
                                <InputGroup size="sm">
                                    <InputGroup.Prepend>
                                        <Button type="button" variant="outline-info" disabled={currentPage === 1}
                                                onClick={this.firstPage}>
                                            <FontAwesomeIcon icon={faFastBackward} /> First
                                        </Button>
                                        <Button type="button" variant="outline-info" disabled={currentPage === 1}
                                                onClick={this.prevPage}>
                                            <FontAwesomeIcon icon={faStepBackward} /> Prev
                                        </Button>
                                    </InputGroup.Prepend>
                                    <FormControl className={"page-num bg-dark"} name="currentPage" value={currentPage}
                                                 onChange={this.changePage}/>
                                    <InputGroup.Append>
                                        <Button type="button" variant="outline-info" disabled={currentPage === totalPages}
                                                onClick={this.nextPage}>
                                            <FontAwesomeIcon icon={faStepForward} /> Next
                                        </Button>
                                        <Button type="button" variant="outline-info" disabled={currentPage === totalPages}
                                                onClick={this.lastPage}>
                                            <FontAwesomeIcon icon={faFastForward} /> Last
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
        passwordObject: state.password
    };
};

const mapDispatchToProps = dispatch => {
    return {
        deletePassword: (passwordId) => dispatch(deletePassword(passwordId))
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(PasswordList);