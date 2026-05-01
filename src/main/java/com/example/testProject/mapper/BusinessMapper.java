package com.example.testProject.mapper;

 import com.example.testProject.dto.business.BusinessRequestDTO;
 import com.example.testProject.dto.business.BusinessResponseDTO;
 import com.example.testProject.entity.Business;

public class BusinessMapper {

    public static Business toEntity(BusinessRequestDTO dto) {
        Business business = new Business();
        business.setName(dto.getName());
        business.setDescription(dto.getDescription());
        business.setEmail(dto.getEmail());
        return business;
    }

    public static BusinessResponseDTO toDTO(Business business) {
        BusinessResponseDTO dto = new BusinessResponseDTO();
        dto.setId(business.getId());
        dto.setName(business.getName());
        dto.setDescription(business.getDescription());
        dto.setEmail(business.getEmail());

        if (business.getOwner() != null) {
            dto.setOwnerId(business.getOwner().getId());
        }

        return dto;
    }
}