package com.example.testProject.service.serviceImpl;

import com.example.testProject.dto.employee.EmployeeRequestDTO;
import com.example.testProject.dto.employee.EmployeeResponseDTO;
import com.example.testProject.entity.Business;
import com.example.testProject.entity.Employee;
import com.example.testProject.mapper.EmployeeMapper;
import com.example.testProject.repository.BusinessRepository;
import com.example.testProject.repository.EmployeeRepository;
import com.example.testProject.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final BusinessRepository businessRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    public EmployeeResponseDTO create(EmployeeRequestDTO dto) {
        Business business = businessRepository.findById(dto.getBusinessId())
                .orElseThrow(() -> new RuntimeException("Business not found"));
        Employee employee = employeeMapper.toEntity(dto);
        employee.setBusiness(business);
        Employee saved = employeeRepository.save(employee);
        return employeeMapper.toDTO(saved);
    }

    @Override
    public EmployeeResponseDTO getById(Long id) {
        return employeeRepository.findById(id)
                .map(employeeMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    @Override
    public List<EmployeeResponseDTO> getAll() {
        return employeeRepository.findAll().stream().map(employeeMapper::toDTO).toList();
    }

    @Override
    public List<EmployeeResponseDTO> getByBusiness(Long businessId) {
        return employeeRepository.findByBusiness_Id(businessId)
                .stream()
                .map(employeeMapper::toDTO)
                .toList();
    }

    @Override
    public EmployeeResponseDTO update(Long id, EmployeeRequestDTO dto) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Business business = businessRepository.findById(dto.getBusinessId())
                .orElseThrow(() -> new RuntimeException("Business not found"));

        employeeMapper.update(dto, employee);
        employee.setBusiness(business);

        Employee saved = employeeRepository.save(employee);
        return employeeMapper.toDTO(saved);
    }

    @Override
    public void delete(Long id) {
        employeeRepository.deleteById(id);
    }
}

