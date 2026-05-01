package com.example.testProject.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponseDTO {
    private String email;
    private String name;
    private String imageUrl;
    private String phone;
    private String token;
    private String role;
}