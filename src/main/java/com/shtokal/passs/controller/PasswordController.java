package com.shtokal.passs.controller;


import com.shtokal.passs.dto.PasswordRequest;
import com.shtokal.passs.dto.AddPasswordRequest;
import com.shtokal.passs.dto.PasswordResponse;
import com.shtokal.passs.dto.ShowPasswordRequest;
import com.shtokal.passs.service.PasswordService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/password")
public class PasswordController {

    private PasswordService passwordService;

    public PasswordController(PasswordService passwordService) {

        this.passwordService = passwordService;
    }

    @PostMapping(value = "/add")
    public ResponseEntity<PasswordRequest> save(@RequestBody AddPasswordRequest passwordRequest) throws Exception {

        HttpHeaders headers = new HttpHeaders();
        if (passwordRequest == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        PasswordRequest add = this.passwordService.add(passwordRequest);
        return new ResponseEntity<>(add, headers, HttpStatus.CREATED);
    }

    @GetMapping(value = "/show")
    public ResponseEntity<String> showPassword (@RequestBody ShowPasswordRequest showPasswordRequest) throws Exception {

        HttpHeaders headers = new HttpHeaders();
        if (showPasswordRequest == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String password = this.passwordService.showPassword(showPasswordRequest);
        return new ResponseEntity<>(password, headers, HttpStatus.CREATED);
    }



    @GetMapping(value = "/allbylogin")
    public ResponseEntity<List<PasswordResponse>> getAllByLogin(@RequestParam String login) {

        HttpHeaders headers = new HttpHeaders();
        if (login == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<PasswordResponse> allByUserLogin = this.passwordService.findAllByUserLogin(login);
        return new ResponseEntity<>(allByUserLogin, headers, HttpStatus.OK);
    }


}
