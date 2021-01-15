package com.shtokal.passs.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OnePasswordResponse {

    String id;
    String login;
    String password;
    String web_address;
    String description;
}
