package com.example.testProject.entity;

import com.example.testProject.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @Column(unique = true)
    private String email;
    private String phone;
    private String password;
    private String imageUrl;
    @Enumerated(EnumType.STRING)
    private Role role;

}
