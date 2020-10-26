package com.shtokal.passs.repository;

import com.shtokal.passs.model.Password;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PasswordRepository extends JpaRepository<Password, Long> {

    List<Password> findAllByUser_Login(String login);
}
