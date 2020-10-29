package com.shtokal.passs.repository;
import com.shtokal.passs.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.security.Principal;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

   User findByLogin(String username);
}
