package com.shtokal.passs.service;

import com.shtokal.passs.config.PassAlgor;
import com.shtokal.passs.dto.PasswordRequest;
import com.shtokal.passs.dto.AddPasswordRequest;
import com.shtokal.passs.dto.PasswordResponse;
import com.shtokal.passs.dto.ShowPasswordRequest;
import com.shtokal.passs.model.Password;
import com.shtokal.passs.model.User;
import com.shtokal.passs.repository.PasswordRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PasswordServiceImplementation implements PasswordService {

    UserService userService;
    PasswordRepository passwordRepository;
    ModelMapper modelMapper;

    public PasswordServiceImplementation(ModelMapper modelMapper, UserService userService, PasswordRepository passwordRepository) {

        this.modelMapper = modelMapper;
        this.userService = userService;
        this.passwordRepository = passwordRepository;
    }

    @Override
    public PasswordRequest add(AddPasswordRequest addPasswordRequest) throws Exception {
        User userByLogin = userService.findUserByLogin(addPasswordRequest.getUserLogin());

        Password password = new Password();
        password.setLogin(addPasswordRequest.getLogin());
        //зашифровать пароль потом
        Key key = PassAlgor.generateKey(addPasswordRequest.getMasterPassword());
        password.setPassword(PassAlgor.encrypt(addPasswordRequest.getPassword(), key));
        password.setWeb_address(addPasswordRequest.getWeb_address());
        password.setDescription(addPasswordRequest.getDescription());
        userByLogin.addPassword(password);
        passwordRepository.save(password);
        PasswordRequest passwordRequest = modelMapper.map(addPasswordRequest, PasswordRequest.class);
        return passwordRequest;


    }

    @Override
    public List<PasswordResponse> findAllByUserLogin(String login) {
        List<Password> allByUser_login = passwordRepository.findAllByUser_Login(login);
        List<PasswordResponse> passwordResponses = new ArrayList<>();
        for (Password pass : allByUser_login) {
            PasswordResponse map = modelMapper.map(pass, PasswordResponse.class);
            map.setUserLogin(login);
            passwordResponses.add(map);

        }

        return passwordResponses;
    }

    @Override
    public String showPassword(ShowPasswordRequest showPasswordRequest) throws Exception {

        Password password = passwordRepository.findById(Long.parseLong(showPasswordRequest.getPasswordId())).get();
        Key key = PassAlgor.generateKey(showPasswordRequest.getMasterPassword());
        return PassAlgor.decrypt(password.getPassword(), key);


    }
}
