package com.example.testProject.repository;

import com.example.testProject.entity.Business;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BusinessRepository extends JpaRepository<Business, Long> {

    Optional<Business> findByOwnerId(Long ownerId);
}