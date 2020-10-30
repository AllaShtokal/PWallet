package com.shtokal.passs.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PasswordResponse {

    private List<PasswordsContent> content;
    private String tatalPages;
    private String tatalElements;
    private String number;

}
