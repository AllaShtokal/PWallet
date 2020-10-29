package com.shtokal.passs.repository;

import com.shtokal.passs.model.ERole;
import com.shtokal.passs.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
