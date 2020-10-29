package com.shtokal.passs.security;

import com.shtokal.passs.model.User;
import com.shtokal.passs.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        try {
            User user = userRepository.findByLogin(login);
            return UserDetailsImpl.build(user);
        } catch (Exception e) {
            throw (new UsernameNotFoundException("User Not Found with username: " + login));
        }


    }

}
