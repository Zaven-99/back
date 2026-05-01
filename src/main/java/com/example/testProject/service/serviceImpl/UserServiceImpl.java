package com.example.testProject.service.serviceImpl;

import com.example.testProject.dto.user.UserRequestDTO;
import com.example.testProject.dto.user.UserResponseDTO;
import com.example.testProject.entity.User;
import com.example.testProject.mapper.UserMapper;
import com.example.testProject.repository.UserRepository;
import com.example.testProject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDTO create(UserRequestDTO dto) {
        User user = userMapper.toEntity(dto);
        User saved = userRepository.save(user);
        return userMapper.toDTO(saved);
    }

    @Override
    public UserResponseDTO getById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public List<UserResponseDTO> getAll() {
        return userRepository.findAll().stream().map(userMapper::toDTO).toList();
    }

    @Override
    public UserResponseDTO update(Long id, UserRequestDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userMapper.update(dto, user);
        User saved = userRepository.save(user);
        return userMapper.toDTO(saved);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}

