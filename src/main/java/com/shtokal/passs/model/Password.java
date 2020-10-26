package com.shtokal.passs.model;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "password")
@Getter
@Setter
public class Password {
    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name="login")
    private String login;

    @Column(name="password")
    private String password;

    @Column(name="web_address")
    private String web_address;

    @Column(name="description")
    private String description;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;




}
