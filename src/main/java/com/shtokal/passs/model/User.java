package com.shtokal.passs.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import static javax.persistence.GenerationType.AUTO;


@Entity
@Table(name = "user")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "login")
    private String login;

    @Column(name = "password_hash")
    private String password_hash;

    @Column(name = "salt")
    private String salt;

    @Column(name = "isPasswordKeptAsHash")
    private Boolean isPasswordKeptAsHash;

    @OneToMany(mappedBy="user")
    private Set<Password> passwords= new HashSet<>();

    public void addPassword(Password password) {
        this.passwords.add(password);
        password.setUser(this);

    }
}
