package com.example.testProject.service.serviceImpl;

import com.example.testProject.dto.auth.AuthRequestDTO;
import com.example.testProject.dto.auth.AuthResponseDTO;
import com.example.testProject.dto.auth.RegisterRequestDTO;
import com.example.testProject.dto.auth.RegisterResponseDTO;
import com.example.testProject.entity.User;
import com.example.testProject.enums.Role;
import com.example.testProject.repository.UserRepository;
import com.example.testProject.service.AuthService;
import com.example.testProject.service.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository,
                           JwtService jwtService,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    // LOGIN
    @Override
    public AuthResponseDTO login(AuthRequestDTO request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Wrong password");
        }

        // 🔥 теперь лучше передавать ID + email
        String token = jwtService.generateToken(
                user.getId(),
                user.getEmail(),
                user.getRole().name()
        );

        AuthResponseDTO response = new AuthResponseDTO();
        response.setToken(token);
        response.setRole(user.getRole().name());
        response.setEmail(user.getEmail());
        response.setName(user.getName());
        response.setPhone(user.getPhone());
        response.setImageUrl(user.getImageUrl());

        return response;
    }

    // USER REGISTER
    @Override
    public RegisterResponseDTO registerUser(RegisterRequestDTO request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already used");
        }

        return registerWithRole(request, Role.USER);
    }

    // BUSINESS REGISTER
    @Override
    public RegisterResponseDTO registerBusiness(RegisterRequestDTO request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already used");
        }

        return registerWithRole(request, Role.BUSINESS);
    }

    private RegisterResponseDTO registerWithRole(RegisterRequestDTO request, Role role) {

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);

        User saved = userRepository.save(user);

        RegisterResponseDTO response = new RegisterResponseDTO();
        response.setUserId(saved.getId());
        response.setEmail(saved.getEmail());
        response.setRole(saved.getRole());

        return response;
    }
}