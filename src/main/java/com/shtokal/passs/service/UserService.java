package com.shtokal.passs.service;

import com.shtokal.passs.dto.ChangeUserPasswordRequest;
import com.shtokal.passs.dto.UserDTO;
import com.shtokal.passs.dto.UserDTORegister;
import com.shtokal.passs.dto.UserResponse;
import com.shtokal.passs.exceptions.LoginExistsException;
import com.shtokal.passs.model.User;

public interface UserService {

    Boolean changePassword(ChangeUserPasswordRequest changeUserPasswordRequest) throws Exception;
    UserResponse add(UserDTORegister userDTORegister) throws LoginExistsException;
    Boolean login(UserDTO userDTO);
    User findUserByLogin(String login);
    Boolean existsByLogin(String login);
    String getPasswordHashValueByPassword(String password, Boolean isPasswordSavedAsHash, String salt);
}
