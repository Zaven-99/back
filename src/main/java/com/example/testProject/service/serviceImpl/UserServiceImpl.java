package com.example.testProject.service.serviceImpl;

import com.example.testProject.dto.user.UserRequestDTO;
import com.example.testProject.dto.user.UserResponseDTO;
import com.example.testProject.entity.User;
import com.example.testProject.mapper.UserMapper;
import com.example.testProject.repository.UserRepository;
import com.example.testProject.security.SecurityUtils;
import com.example.testProject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    // CREATE
    @Override
    public UserResponseDTO create(UserRequestDTO dto) {
        User user = userMapper.toEntity(dto);
        return userMapper.toDTO(userRepository.save(user));
    }

    // GET BY ID
    @Override
    public UserResponseDTO getById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // GET ALL
    @Override
    public List<UserResponseDTO> getAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDTO)
                .toList();
    }

    // UPDATE BY ID
    @Override
    public UserResponseDTO update(Long id, UserRequestDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userMapper.update(dto, user);

        return userMapper.toDTO(userRepository.save(user));
    }

    // DELETE BY ID
    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    // =========================
    // 🔥 JWT CURRENT USER LOGIC
    // =========================

    @Override
    public UserResponseDTO getCurrentUser() {
        User user = SecurityUtils.currentUser();
        return userMapper.toDTO(user);
    }

    @Override
    public UserResponseDTO updateCurrentUser(UserRequestDTO dto) {
        User user = SecurityUtils.currentUser();

        userMapper.update(dto, user);

        return userMapper.toDTO(userRepository.save(user));
    }

    @Override
    public void deleteCurrentUser() {
        User user = SecurityUtils.currentUser();
        userRepository.delete(user);
    }
}