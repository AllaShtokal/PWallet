package com.shtokal.passs.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddPasswordRequest {

    private String masterPassword;
    private String userLogin;
    private String login;
    private String password;
    private String web_address;
    private String description;
}
