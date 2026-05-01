package com.example.testProject.dto.auth;

 import com.example.testProject.enums.Role;
 import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestDTO {

    private String email;
    private String password;

     private Role role;
}