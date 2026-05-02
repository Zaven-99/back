package com.example.testProject.repository;

import com.example.testProject.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByBusiness_Id(Long businessId);
    List<Review> findByUser_Id(Long userId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.business.id = :businessId")
    Double getAverageRating(@Param("businessId") Long businessId);

    Long countByBusiness_Id(Long businessId);

    Page<Review> findByBusiness_Id(Long businessId, Pageable pageable);
}

