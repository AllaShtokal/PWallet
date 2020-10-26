package com.shtokal.passs.controller;

import com.shtokal.passs.dto.ChangeUserPasswordRequest;
import com.shtokal.passs.dto.UserDTO;
import com.shtokal.passs.dto.UserDTORegister;
import com.shtokal.passs.dto.UserResponse;
import com.shtokal.passs.exceptions.LoginExistsException;
import com.shtokal.passs.service.UserService;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1/user")
public class UserController {


    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/add")
    public ResponseEntity<UserResponse> save(@RequestBody UserDTORegister userDTORegister) {

        HttpHeaders headers = new HttpHeaders();
        if (userDTORegister == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        UserResponse userResponse = this.userService.add(userDTORegister);
        return new ResponseEntity<>(userResponse, headers, HttpStatus.CREATED);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<Boolean> login(@RequestBody UserDTO userDTO) {

        HttpHeaders headers = new HttpHeaders();
        if (userDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Boolean loginResult = this.userService.login(userDTO);

        return new ResponseEntity<>(loginResult, headers, HttpStatus.OK);
    }

    @PostMapping(value = "/change")
    public ResponseEntity<Boolean> changePassword(@RequestBody ChangeUserPasswordRequest uRequest) {

        HttpHeaders headers = new HttpHeaders();
        if (uRequest == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Boolean changeResult = this.userService.changePassword(uRequest);

        return new ResponseEntity<>(changeResult, headers, HttpStatus.OK);
    }


}
