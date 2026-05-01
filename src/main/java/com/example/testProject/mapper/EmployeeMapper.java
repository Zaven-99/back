package com.example.testProject.mapper;

import com.example.testProject.dto.employee.EmployeeRequestDTO;
import com.example.testProject.dto.employee.EmployeeResponseDTO;
import com.example.testProject.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapStructConfig.class)
public interface EmployeeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "business", ignore = true)
    Employee toEntity(EmployeeRequestDTO dto);

    @Mapping(target = "businessId", source = "business.id")
    EmployeeResponseDTO toDTO(Employee employee);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "business", ignore = true)
    void update(EmployeeRequestDTO dto, @MappingTarget Employee employee);
}

