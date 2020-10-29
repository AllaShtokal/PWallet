import {LOGIN_REQUEST, LOGOUT_REQUEST, SUCCESS, FAILURE} from './authTypes';
import axios from "axios";

export const authenticateUser = (login, password) => {
    return dispatch => {
        dispatch(loginRequest());
        const user = {
            login: login,
            password: password
        };
        axios.post("http://localhost:8080/api/v1/user/login", user)
            .then((response) => {
                if (response.data === true) {
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