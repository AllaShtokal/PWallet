package com.shtokal.passs.controller;

import com.shtokal.passs.dto.ChangeUserPasswordRequest;
import com.shtokal.passs.dto.UserDTO;
import com.shtokal.passs.dto.UserDTORegister;
import com.shtokal.passs.dto.UserResponse;
import com.shtokal.passs.exceptions.LoginExistsException;
import com.shtokal.passs.security.JwtUtils;
import com.shtokal.passs.service.LogService;
import com.shtokal.passs.service.UserService;
import org.mockito.Mock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.assertEquals;

public class UserControllerTest {

    @Mock
    private UserService mockUserService;
    @Mock
    private AuthenticationManager mockAuthenticationManager;
    @Mock
    private JwtUtils mockJwtUtils;
    @Mock
    private LogService mockLogService;

    private UserController userControllerUnderTest;

    @BeforeMethod
    public void setUp() {
        initMocks(this);
        userControllerUnderTest = new UserController(mockUserService, mockAuthenticationManager, mockJwtUtils, mockLogService);
    }



    @Test
    public void testSave() {

        final UserDTORegister userDTORegister = new UserDTORegister();
        userDTORegister.setLogin("login");
        userDTORegister.setPassword("password");
        userDTORegister.setIsPasswordSavedAsHash(false);
        when(mockUserService.existsByLogin("login")).thenReturn(false);

        final UserResponse userResponse = new UserResponse();
        userResponse.setUserId("1");
        userResponse.setUserLogin("userLogin");
        when(mockUserService.add(any(UserDTORegister.class))).thenReturn(userResponse);

        HttpHeaders headers = new HttpHeaders();
        assertEquals(userControllerUnderTest.save(userDTORegister),
                new ResponseEntity<>(userResponse,headers,HttpStatus.CREATED) );


    }


//    @Test
//    public void testLogin() {
//        final UserDTO userDTO = new UserDTO();
//        userDTO.setLogin("login");
//        userDTO.setPassword("password");
//
//        final ResponseEntity<Boolean> expectedResult = new ResponseEntity<>(false, HttpStatus.OK);
//
//        when(mockUserService.login(any(UserDTO.class), request)).thenReturn(false);
//
//        final ResponseEntity<Integer> result = userControllerUnderTest.login(userDTO,request);
//
//        assertEquals(expectedResult, result);
//    }

    @Test
    public void testChangePassword() throws Exception {

        final ChangeUserPasswordRequest uRequest = new ChangeUserPasswordRequest();
        uRequest.setLogin("login");
        uRequest.setOldPassword("oldPassword");
        uRequest.setNewPassword("newPassword");
        uRequest.setIsPasswordSavedAsHash(false);

        final ResponseEntity<Boolean> expectedResult = new ResponseEntity<>(false, HttpStatus.OK);
        when(mockUserService.changePassword(any(ChangeUserPasswordRequest.class))).thenReturn(false);

        final ResponseEntity<Boolean> result = userControllerUnderTest.changePassword(uRequest);

        assertEquals(expectedResult, result);
    }

    @Test(expectedExceptions = {Exception.class})
    public void testChangePasswordThrowsException() throws Exception {

        final ChangeUserPasswordRequest uRequest = new ChangeUserPasswordRequest();
        uRequest.setLogin("login");
        uRequest.setOldPassword("oldPassword");
        uRequest.setNewPassword("newPassword");
        uRequest.setIsPasswordSavedAsHash(false);

        when(mockUserService.changePassword(any(ChangeUserPasswordRequest.class))).thenThrow(Exception.class);

        userControllerUnderTest.changePassword(uRequest);
    }
}
