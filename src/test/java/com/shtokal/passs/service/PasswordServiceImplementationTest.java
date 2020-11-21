package com.shtokal.passs.service;

import com.shtokal.passs.dto.*;
import com.shtokal.passs.model.Password;
import com.shtokal.passs.model.Role;
import com.shtokal.passs.model.User;
import com.shtokal.passs.repository.PasswordRepository;
import com.shtokal.passs.repository.UserRepository;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.*;

import static com.shtokal.passs.model.ERole.ROLE_USER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;



public class PasswordServiceImplementationTest {

    @Mock
    private ModelMapper mockModelMapper;
    @Mock
    private UserRepository mockUserService;
    @Mock
    private PasswordRepository mockPasswordRepository;
    private PasswordServiceImplementation passwordServiceImplementationUnderTest;

    @BeforeMethod
    public void setUp() {
        initMocks(this);
        passwordServiceImplementationUnderTest = new PasswordServiceImplementation(mockModelMapper, mockUserService, mockPasswordRepository);
    }

    @Test
    public void testAdd() throws Exception {
        final AddPasswordRequest addPasswordRequest = new AddPasswordRequest();
        addPasswordRequest.setMasterPassword("masterPassword");
        addPasswordRequest.setUserLogin("userLogin");
        addPasswordRequest.setLogin("login");
        addPasswordRequest.setPassword("password");
        addPasswordRequest.setWeb_address("web_address");
        addPasswordRequest.setDescription("description");

        User user = new User();
        user.setId(1L);
        user.setIsPasswordKeptAsHash(true);
        user.setPassword_hash("123456");
        user.setSalt("salt");
        user.setLogin("userLogin");
        Set <Role> roles =new HashSet<>();
        roles.add(new Role(ROLE_USER));
        user.setRoles(roles);

        final Password password = new Password();
        password.setId(0L);
        password.setLogin("login");
        password.setPassword("password");
        password.setWeb_address("web_address");
        password.setDescription("description");

        when(mockPasswordRepository.save(any(Password.class))).thenReturn(password);
        user.addPassword(password);
        when(mockUserService.findByLogin("userLogin")).thenReturn(user);

        final PasswordRequest passwordRequest = new PasswordRequest();
        passwordRequest.setUserLogin("userLogin");
        passwordRequest.setLogin("login");
        passwordRequest.setPassword("password");
        passwordRequest.setWeb_address("web_address");
        passwordRequest.setDescription("description");
        when(mockModelMapper.map(any(Object.class), eq(PasswordRequest.class))).thenReturn(passwordRequest);

        final PasswordRequest result = passwordServiceImplementationUnderTest.add(addPasswordRequest);

        assertEquals(result, passwordRequest);
    }

    @Test
    public void testFindAllByUserLogin() {
        final Password password = new Password();
        password.setId(0L);
        password.setLogin("login");
        password.setPassword("password");
        password.setWeb_address("web_address");
        password.setDescription("description");
        password.setUser(new User());
        final List<Password> passwords = Collections.singletonList(password);
        when(mockPasswordRepository.findAllByUser_Login("login")).thenReturn(passwords);

        final Password password1 = new Password();
        password1.setId(0L);
        password1.setLogin("login");
        password1.setPassword("password");
        password1.setWeb_address("web_address");
        password1.setDescription("description");
        password1.setUser(new User());
        final List<Password> passwords1 = Collections.singletonList(password1);
        when(mockPasswordRepository.findAllByUser_Login(eq("login"), any(Pageable.class))).thenReturn(passwords1);

        final PasswordsContent passwordsContent = new PasswordsContent();
        passwordsContent.setId("0");
        passwordsContent.setLogin("login");
        passwordsContent.setPassword("password");
        passwordsContent.setWeb_address("web_address");
        passwordsContent.setDescription("description");
        when(mockModelMapper.map(any(Object.class), eq(PasswordsContent.class))).thenReturn(passwordsContent);

        final PasswordResponse result = passwordServiceImplementationUnderTest.findAllByUserLogin(
                "login",
                PageRequest.of(0, 1));

        PasswordResponse passwordResponse = new PasswordResponse();

        passwordResponse.setTatalPages("1");
        passwordResponse.setTatalElements("1");
        passwordResponse.setNumber("0");
        List<PasswordsContent> content = new ArrayList<>();
        content.add(passwordsContent);

        assertEquals(result, passwordResponse);


    }

    @Test
    public void testShowPassword() throws Exception {

        final ShowPasswordRequest showPasswordRequest = new ShowPasswordRequest();
        showPasswordRequest.setPasswordId("0");
        showPasswordRequest.setMasterPassword("masterPassword");

        final Password password1 = new Password();
        password1.setId(0L);
        password1.setLogin("login");
        password1.setPassword("C/+sg8Yg7BJ4yVK4Q9ZiXA==");
        password1.setWeb_address("web_address");
        password1.setDescription("description");
        password1.setUser(new User());
        final Optional<Password> password = Optional.of(password1);
        when(mockPasswordRepository.findById(0L)).thenReturn(password);

        final String result = passwordServiceImplementationUnderTest.showPassword(showPasswordRequest);
        System.out.println(result);

        assertEquals("result", result);
    }


    @Test
    public void testChangeAllUsersPasswords() throws Exception {

        final Password password = new Password();
        password.setId(0L);
        password.setLogin("login");
        password.setPassword("");
        password.setWeb_address("web_address");
        password.setDescription("description");
        password.setUser(new User());
        final List<Password> passwords = Collections.singletonList(password);
        when(mockPasswordRepository.findAllByUser_Login("login")).thenReturn(passwords);

        final Password password1 = new Password();
        password1.setId(0L);
        password1.setLogin("login");
        password1.setPassword("passwordpassword");
        password1.setWeb_address("web_address");
        password1.setDescription("description");
        password1.setUser(new User());
        when(mockPasswordRepository.save(any(Password.class))).thenReturn(password1);


        final Boolean result = passwordServiceImplementationUnderTest.changeAllUsersPasswords(
                "login", "oldMasterPass", "NewMasterPass");

        assertTrue(result);
    }


}
