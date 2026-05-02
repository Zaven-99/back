package com.example.testProject.service;

import com.example.testProject.dto.employee.EmployeeRequestDTO;
import com.example.testProject.dto.employee.EmployeeResponseDTO;

import java.util.List;

public interface EmployeeService {

    EmployeeResponseDTO create(EmployeeRequestDTO dto);

    EmployeeResponseDTO getById(Long id);

    List<EmployeeResponseDTO> getAll();

    List<EmployeeResponseDTO> getByBusiness(Long businessId);

    EmployeeResponseDTO update(Long id, EmployeeRequestDTO dto);

    void delete(Long id);
}