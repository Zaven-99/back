package com.example.testProject.dto.business;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessResponseDTO {
    private Long id;
    private String name;
    private String description;
    private String email;
    private Long ownerId;
}