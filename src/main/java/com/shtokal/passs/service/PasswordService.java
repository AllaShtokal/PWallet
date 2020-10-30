package com.shtokal.passs.service;

import com.shtokal.passs.dto.PasswordRequest;
import com.shtokal.passs.dto.AddPasswordRequest;
import com.shtokal.passs.dto.PasswordResponse;
import com.shtokal.passs.dto.ShowPasswordRequest;

import org.springframework.data.domain.Pageable;

public interface PasswordService {
    PasswordRequest add(AddPasswordRequest passwordRequest) throws Exception;
    PasswordResponse findAllByUserLogin(String login, Pageable pageable);
    String showPassword(ShowPasswordRequest showPasswordRequest) throws Exception;
    Boolean changeAllUsersPasswords(String login, String oldMasterPass, String NewMasterPass) throws Exception;
}
