package com.example.testProject.service.serviceImpl;

import com.example.testProject.dto.business.BusinessRequestDTO;
import com.example.testProject.dto.business.BusinessResponseDTO;
import com.example.testProject.entity.Business;
import com.example.testProject.entity.User;
import com.example.testProject.error.NotFoundException;
import com.example.testProject.mapper.BusinessMapper;
import com.example.testProject.repository.BusinessRepository;
import com.example.testProject.repository.UserRepository;
import com.example.testProject.security.SecurityUtils;
import com.example.testProject.service.BusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BusinessServiceImpl implements BusinessService {

    private final BusinessRepository businessRepository;
    private final UserRepository userRepository;
    private final BusinessMapper businessMapper;

    @Override
    public BusinessResponseDTO create(BusinessRequestDTO dto) {
        User ownerPrincipal = SecurityUtils.currentUser();
        User owner = userRepository.findById(ownerPrincipal.getId())
                .orElseThrow(() -> new NotFoundException("Owner not found"));
        Business business = businessMapper.toEntity(dto);
        business.setOwner(owner);
        Business saved = businessRepository.save(business);
        return businessMapper.toDTO(saved);
    }

    @Override
    public BusinessResponseDTO getById(Long id) {
        return businessRepository.findById(id)
                .map(businessMapper::toDTO)
                .orElseThrow(() -> new NotFoundException("Business not found"));
    }

    @Override
    public List<BusinessResponseDTO> getAll() {
        return businessRepository.findAll().stream().map(businessMapper::toDTO).toList();
    }

    @Override
    public BusinessResponseDTO update(Long id, BusinessRequestDTO dto) {
        Business business = businessRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Business not found"));
        businessMapper.update(dto, business);
        Business saved = businessRepository.save(business);
          return businessMapper.toDTO(saved);
    }


    @Override
    public void delete(Long id) {
        businessRepository.deleteById(id);
    }
}

