package com.example.testProject.service.serviceImpl;

import com.example.testProject.dto.employee.EmployeeRequestDTO;
import com.example.testProject.dto.employee.EmployeeResponseDTO;
import com.example.testProject.entity.Business;
import com.example.testProject.entity.Employee;
import com.example.testProject.entity.User;
import com.example.testProject.mapper.EmployeeMapper;
import com.example.testProject.repository.BusinessRepository;
import com.example.testProject.repository.EmployeeRepository;
import com.example.testProject.security.SecurityUtils;
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

    // CREATE (только владелец бизнеса)
    @Override
    public EmployeeResponseDTO create(EmployeeRequestDTO dto) {

        User current = SecurityUtils.currentUser();

        Business business = businessRepository.findById(dto.getBusinessId())
                .orElseThrow(() -> new RuntimeException("Business not found"));

        if (!business.getOwner().getId().equals(current.getId())) {
            throw new RuntimeException("Not your business");
        }

        Employee employee = employeeMapper.toEntity(dto);
        employee.setBusiness(business);

        return employeeMapper.toDTO(employeeRepository.save(employee));
    }

    // GET BY ID
    @Override
    public EmployeeResponseDTO getById(Long id) {
        return employeeRepository.findById(id)
                .map(employeeMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    // GET ALL
    @Override
    public List<EmployeeResponseDTO> getAll() {
        return employeeRepository.findAll()
                .stream()
                .map(employeeMapper::toDTO)
                .toList();
    }

    // FILTER BY BUSINESS
    @Override
    public List<EmployeeResponseDTO> getByBusiness(Long businessId) {
        return employeeRepository.findByBusiness_Id(businessId)
                .stream()
                .map(employeeMapper::toDTO)
                .toList();
    }

    // UPDATE (только владелец бизнеса)
    @Override
    public EmployeeResponseDTO update(Long id, EmployeeRequestDTO dto) {

        User current = SecurityUtils.currentUser();

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if (!employee.getBusiness().getOwner().getId().equals(current.getId())) {
            throw new RuntimeException("Not your business");
        }

        Business business = businessRepository.findById(dto.getBusinessId())
                .orElseThrow(() -> new RuntimeException("Business not found"));

        employeeMapper.update(dto, employee);
        employee.setBusiness(business);

        return employeeMapper.toDTO(employeeRepository.save(employee));
    }

    // DELETE (только владелец бизнеса)
    @Override
    public void delete(Long id) {

        User current = SecurityUtils.currentUser();

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if (!employee.getBusiness().getOwner().getId().equals(current.getId())) {
            throw new RuntimeException("Not your business");
        }

        employeeRepository.delete(employee);
    }
}