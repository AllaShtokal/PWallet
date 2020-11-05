package com.shtokal.passs.controller;

import com.shtokal.passs.dto.ChangeUserPasswordRequest;
import com.shtokal.passs.dto.UserDTO;
import com.shtokal.passs.dto.UserDTORegister;
import com.shtokal.passs.dto.UserResponse;
import com.shtokal.passs.model.User;
import com.shtokal.passs.response.JwtResponse;
import com.shtokal.passs.response.MessageResponse;
import com.shtokal.passs.security.JwtUtils;
import com.shtokal.passs.security.UserDetailsImpl;
import com.shtokal.passs.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.userService = userService;
    }

    @PostMapping(value = "/add")
    public ResponseEntity<UserResponse> save(@RequestBody UserDTORegister userDTORegister) {
        if (userDTORegister == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (userService.existsByLogin(userDTORegister.getLogin())) {
            return ResponseEntity
                    .badRequest()
                    .body(new UserResponse());
        }

        HttpHeaders headers = new HttpHeaders();

        UserResponse add = userService.add(userDTORegister);
        return new ResponseEntity<UserResponse>(add, headers, HttpStatus.CREATED);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<Boolean> login(@RequestBody UserDTO userDTO) {
        if (userDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        HttpHeaders headers = new HttpHeaders();

        Boolean login = this.userService.login(userDTO);

        return ResponseEntity.ok(login);
    }

    @PostMapping(value = "/change")
    public ResponseEntity<Boolean> changePassword(@RequestBody ChangeUserPasswordRequest uRequest) throws Exception {

        HttpHeaders headers = new HttpHeaders();
        if (uRequest == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Boolean changeResult = this.userService.changePassword(uRequest);


        return new ResponseEntity<>(changeResult, headers, HttpStatus.OK);


    }


}
