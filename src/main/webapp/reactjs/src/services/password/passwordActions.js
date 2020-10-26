import {SAVE_password_REQUEST, FETCH_password_REQUEST, UPDATE_password_REQUEST, DELETE_password_REQUEST, password_SUCCESS, PASS_FAILURE} from "./passwordTypes";
import axios from 'axios';

export const savePassword = password => {
    return dispatch => {
        dispatch(savePasswordRequest());
        axios.post("http://localhost:8081/rest/passwords", password)
            .then(response => {
                dispatch(passwordSuccess(response.data));
            })
            .catch(error => {
                dispatch(passwordFailure(error));
            });
    };
};

const savePasswordRequest = () => {
    return {
        type: SAVE_password_REQUEST
    };
};

const fetchpasswordRequest = () => {
    return {
        type: FETCH_password_REQUEST
    };
};

export const fetchpassword = passwordId => {
    return dispatch => {
        dispatch(fetchpasswordRequest());
        axios.get("http://localhost:8081/rest/passwords/"+passwordId)
            .then(response => {
                dispatch(passwordSuccess(response.data));
            })
            .catch(error => {
                dispatch(passwordFailure(error));
            });
    };
};

const updatepasswordRequest = () => {
    return {
        type: UPDATE_password_REQUEST
    };
};

export const updatepassword = password => {
    return dispatch => {
        dispatch(updatepasswordRequest());
        axios.put("http://localhost:8081/rest/passwords", password)
            .then(response => {
                dispatch(passwordSuccess(response.data));
            })
            .catch(error => {
                dispatch(passwordFailure(error));
            });
    };
};

const deletepasswordRequest = () => {
    return {
        type: DELETE_password_REQUEST
    };
};

export const deletePassword = passwordId => {
    return dispatch => {
        dispatch(deletepasswordRequest());
        axios.delete("http://localhost:8081/rest/passwords/"+passwordId)
            .then(response => {
                dispatch(passwordSuccess(response.data));
            })
            .catch(error => {
                dispatch(passwordFailure(error));
            });
    };
};

const passwordSuccess = password => {
    return {
        type: password_SUCCESS,
        payload: password
    };
};

const passwordFailure = error => {
    return {
        type: PASS_FAILURE,
        payload: error
    };
};