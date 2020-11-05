package com.shtokal.passs.service;

import com.shtokal.passs.config.PassAlgor;
import com.shtokal.passs.dto.*;
import com.shtokal.passs.model.Password;
import com.shtokal.passs.model.User;
import com.shtokal.passs.repository.PasswordRepository;
import com.shtokal.passs.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;


import java.security.Key;
import java.util.ArrayList;
import java.util.List;

@Service
public class PasswordServiceImplementation implements PasswordService {

    UserRepository userService;
    PasswordRepository passwordRepository;
    ModelMapper modelMapper;

    public PasswordServiceImplementation(ModelMapper modelMapper, UserRepository userService, PasswordRepository passwordRepository) {

        this.modelMapper = modelMapper;
        this.userService = userService;
        this.passwordRepository = passwordRepository;
    }

    @Override
    public PasswordRequest add(AddPasswordRequest addPasswordRequest) throws Exception {
        User userByLogin = userService.findByLogin(addPasswordRequest.getUserLogin());

        Password password = new Password();
        password.setLogin(addPasswordRequest.getLogin());
        Key key = PassAlgor.generateKey(addPasswordRequest.getMasterPassword());
        password.setPassword(PassAlgor.encrypt(addPasswordRequest.getPassword(), key));
        password.setWeb_address(addPasswordRequest.getWeb_address());
        password.setDescription(addPasswordRequest.getDescription());
        userByLogin.addPassword(password);
        passwordRepository.save(password);
        return modelMapper.map(addPasswordRequest, PasswordRequest.class);
    }


    @Override
    public PasswordResponse findAllByUserLogin(String login, Pageable pageable) {
        Integer totalElements = passwordRepository.findAllByUser_Login(login).size();
        Integer totalPages;
        if(totalElements%pageable.getPageSize()==0)
        totalPages=totalElements/pageable.getPageSize();
        else totalPages=totalElements/pageable.getPageSize()+1;


        List<Password> allByUser_login = passwordRepository.findAllByUser_Login(login, pageable);
        PasswordResponse passwordRespons = new PasswordResponse();
        List<PasswordsContent> content = new ArrayList<>();
        for (Password pass : allByUser_login) {
            PasswordsContent map = modelMapper.map(pass, PasswordsContent.class);
            content.add(map);
        }
        passwordRespons.setContent(content);
        passwordRespons.setTatalElements(totalElements.toString());
        passwordRespons.setTatalPages(totalPages.toString());
        passwordRespons.setNumber(String.valueOf(pageable.getPageNumber()));
        return passwordRespons;
    }

    @Override
    public String showPassword(ShowPasswordRequest showPasswordRequest) throws Exception {

        Password password = passwordRepository.findById(Long.parseLong(showPasswordRequest.getPasswordId())).get();
        Key key = PassAlgor.generateKey(showPasswordRequest.getMasterPassword());
        return PassAlgor.decrypt(password.getPassword(), key);


    }

    @Override
    public Boolean changeAllUsersPasswords(String login, String oldMasterPass, String NewMasterPass) throws Exception {
        List<Password> allByUser_login = passwordRepository.findAllByUser_Login(login);

        for (Password p : allByUser_login) {
            String decrypt = PassAlgor.decrypt(p.getPassword(), PassAlgor.generateKey(oldMasterPass));
            p.setPassword(PassAlgor.encrypt(decrypt, PassAlgor.generateKey(NewMasterPass)));
            passwordRepository.save(p);
        }
        return true;


    }
}
