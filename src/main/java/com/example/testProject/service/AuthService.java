package com.example.testProject.service;

import com.example.testProject.dto.auth.AuthRequestDTO;
import com.example.testProject.dto.auth.AuthResponseDTO;
import com.example.testProject.dto.auth.RegisterRequestDTO;
import com.example.testProject.dto.auth.RegisterResponseDTO;

public interface AuthService {
    AuthResponseDTO login(AuthRequestDTO request);
    RegisterResponseDTO registerUser(RegisterRequestDTO request);
    RegisterResponseDTO registerBusiness(RegisterRequestDTO request);

}
