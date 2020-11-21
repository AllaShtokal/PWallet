import {LOGIN_REQUEST, LOGOUT_REQUEST, SUCCESS, FAILURE} from './authTypes';
import axios from "axios";

export const authenticateUser = (login, password) => {
    return dispatch => {
        dispatch(loginRequest());
        localStorage.setItem('masterPassword', password);
        localStorage.setItem('login', login);
        const user = {
            login: login,
            password: password
        };
        axios.post("http://localhost:8080/api/v1/user/login", user)
            .then((response) => {
                if (response.data === 0) {
                    dispatch(success(true));
                } else {
                    dispatch(failure());
                }
            });


    };
};

const loginRequest = () => {
    return {
        type: LOGIN_REQUEST
    };
};

export const logoutUser = () => {
    return dispatch => {

        localStorage.removeItem('login')
        localStorage.removeItem('masterPassword')
        dispatch(logoutRequest());
        dispatch(success(false));
    };
};

const logoutRequest = () => {
    return {
        type: LOGOUT_REQUEST
    };
};

const success = isLoggedIn => {
    return {
        type: SUCCESS,
        payload: isLoggedIn
    };
};

const failure = () => {
    return {
        type: FAILURE,
        payload: false
    };
};