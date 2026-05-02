package com.example.testProject.service;

import com.example.testProject.dto.user.UserRequestDTO;
import com.example.testProject.dto.user.UserResponseDTO;

import java.util.List;

public interface UserService {

    UserResponseDTO create(UserRequestDTO dto);

    UserResponseDTO getById(Long id);

    List<UserResponseDTO> getAll();

    UserResponseDTO update(Long id, UserRequestDTO dto);

    void delete(Long id);

    // 🔥 CURRENT USER METHODS (JWT)
    UserResponseDTO getCurrentUser();

    UserResponseDTO updateCurrentUser(UserRequestDTO dto);

    void deleteCurrentUser();
}