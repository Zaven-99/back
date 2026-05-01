package com.example.testProject.repository;

import com.example.testProject.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByBusiness_Id(Long businessId);
    List<Review> findByUser_Id(Long userId);
}

