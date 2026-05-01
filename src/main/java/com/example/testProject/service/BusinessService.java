package com.example.testProject.service;

import com.example.testProject.dto.business.BusinessRequestDTO;
import com.example.testProject.dto.business.BusinessResponseDTO;

import java.util.List;

public interface BusinessService {
    BusinessResponseDTO create(BusinessRequestDTO dto);
    BusinessResponseDTO getById(Long id);
    List<BusinessResponseDTO> getAll();
    BusinessResponseDTO update(Long id, BusinessRequestDTO dto);
    void delete(Long id);
}

