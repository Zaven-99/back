package com.example.testProject.repository;

import com.example.testProject.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByBusiness_Id(Long businessId);
}

