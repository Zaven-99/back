package com.example.testProject.dto.auth;

 import com.example.testProject.enums.Role;
 import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterResponseDTO {

    private Long userId;
    private String email;
    private Role role;
}