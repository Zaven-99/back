package com.example.testProject.mapper;

import com.example.testProject.dto.business.BusinessRequestDTO;
import com.example.testProject.dto.business.BusinessResponseDTO;
import com.example.testProject.entity.Business;
import org.mapstruct.*;

@Mapper(config = MapStructConfig.class)
public interface BusinessMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    Business toEntity(BusinessRequestDTO dto);

    @Mapping(target = "ownerId", source = "owner.id")
    BusinessResponseDTO toDTO(Business business);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    void update(BusinessRequestDTO dto, @MappingTarget Business business);
}