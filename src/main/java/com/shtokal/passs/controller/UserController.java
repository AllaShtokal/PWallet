package com.shtokal.passs.controller;

import com.shtokal.passs.dto.ChangeUserPasswordRequest;
import com.shtokal.passs.dto.UserDTO;
import com.shtokal.passs.dto.UserDTORegister;
import com.shtokal.passs.model.User;
import com.shtokal.passs.response.JwtResponse;
import com.shtokal.passs.response.MessageResponse;
import com.shtokal.passs.security.JwtUtils;
import com.shtokal.passs.security.UserDetailsImpl;
import com.shtokal.passs.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1/user")
public class UserController {


    private UserService userService;
    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils;

    public UserController(UserService userService, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping(value = "/add")
    public ResponseEntity<?> save(@RequestBody UserDTORegister userDTORegister) {
        if (userDTORegister == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (userService.existsByLogin(userDTORegister.getLogin())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        HttpHeaders headers = new HttpHeaders();

        userService.add(userDTORegister);
        return new ResponseEntity<>(new MessageResponse("User registered successfully!"), headers, HttpStatus.CREATED);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<Boolean> login(@RequestBody UserDTO userDTO) {
        if (userDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        //надо брать не пароль а закодированій пароль
//        User userByLogin = userService.findUserByLogin(userDTO.getLogin());
//        String passwordHashValueByPassword = userService.getPasswordHashValueByPassword(userDTO.getLogin(),
//                userByLogin.getIsPasswordKeptAsHash(),
//                userByLogin.getSalt());

//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(userDTO.getLogin(), passwordHashValueByPassword));
//        SecurityContext context = SecurityContextHolder.getContext();
//        context.setAuthentication(authentication);
//
//        String jwt = jwtUtils.generateJwtToken(authentication);
//
//        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
//        List<String> roles = userDetails.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.toList());

        HttpHeaders headers = new HttpHeaders();

        Boolean login = this.userService.login(userDTO);

        return ResponseEntity.ok(login);
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
