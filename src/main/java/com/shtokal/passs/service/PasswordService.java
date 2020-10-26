package com.shtokal.passs.service;

import com.shtokal.passs.dto.PasswordRequest;
import com.shtokal.passs.dto.AddPasswordRequest;
import com.shtokal.passs.dto.PasswordResponse;
import com.shtokal.passs.dto.ShowPasswordRequest;

import java.util.List;

public interface PasswordService {
    PasswordRequest add(AddPasswordRequest passwordRequest) throws Exception;
    List<PasswordResponse> findAllByUserLogin(String login);
    String showPassword(ShowPasswordRequest showPasswordRequest) throws Exception;
}
