package com.shtokal.passs.controller;


import com.shtokal.passs.dto.PasswordRequest;
import com.shtokal.passs.dto.AddPasswordRequest;
import com.shtokal.passs.dto.PasswordResponse;
import com.shtokal.passs.dto.ShowPasswordRequest;
import com.shtokal.passs.service.PasswordService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1/password")
public class PasswordController {

    private final PasswordService passwordService;

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

    @PostMapping(value = "/show")
    public ResponseEntity<String> showPassword (@RequestBody ShowPasswordRequest showPasswordRequest) throws Exception {

        HttpHeaders headers = new HttpHeaders();
        if (showPasswordRequest == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String password = this.passwordService.showPassword(showPasswordRequest);
        return new ResponseEntity<>(password, headers, HttpStatus.CREATED);
    }



    @GetMapping(value = "/allbylogin")
    public ResponseEntity<PasswordResponse> getAllByLogin(@RequestParam String login,
                                                                @RequestParam String  pageNumber,
                                                                @RequestParam String  pageSize ) {

        HttpHeaders headers = new HttpHeaders();
        if (login == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Pageable pageable = PageRequest.of(Integer.parseInt(pageNumber), Integer.parseInt(pageSize));

       PasswordResponse resp = this.passwordService.findAllByUserLogin(login, pageable);
        return new ResponseEntity<>(resp, headers, HttpStatus.OK);
    }


}
