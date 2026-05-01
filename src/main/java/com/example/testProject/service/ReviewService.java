package com.example.testProject.service;

import com.example.testProject.dto.review.ReviewRequestDTO;
import com.example.testProject.dto.review.ReviewResponseDTO;

import java.util.List;

public interface ReviewService {
    ReviewResponseDTO create(ReviewRequestDTO dto);
    ReviewResponseDTO getById(Long id);
    List<ReviewResponseDTO> getAll();
    List<ReviewResponseDTO> getByBusiness(Long businessId);
    List<ReviewResponseDTO> getByUser(Long userId);
    ReviewResponseDTO update(Long id, ReviewRequestDTO dto);
    void delete(Long id);
}

