package com.example.testProject.controller;

import com.example.testProject.dto.auth.AuthRequestDTO;
import com.example.testProject.dto.auth.AuthResponseDTO;
import com.example.testProject.dto.auth.RegisterRequestDTO;
import com.example.testProject.dto.auth.RegisterResponseDTO;
import com.example.testProject.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register/user")
    public ResponseEntity<RegisterResponseDTO> registerUser(@Valid @RequestBody RegisterRequestDTO request) {
        return ResponseEntity.ok(authService.registerUser(request));
    }

    @PostMapping("/register/business")
    public ResponseEntity<RegisterResponseDTO> registerBusiness(@Valid @RequestBody RegisterRequestDTO request) {
        return ResponseEntity.ok(authService.registerBusiness(request));
    }
}