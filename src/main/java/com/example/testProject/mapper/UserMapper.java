package com.example.testProject.mapper;

import com.example.testProject.dto.user.UserRequestDTO;
import com.example.testProject.dto.user.UserResponseDTO;
import com.example.testProject.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapStructConfig.class)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    User toEntity(UserRequestDTO dto);

    UserResponseDTO toDTO(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    void update(UserRequestDTO dto, @MappingTarget User user);
}