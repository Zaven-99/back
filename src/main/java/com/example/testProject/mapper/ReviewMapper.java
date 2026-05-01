package com.example.testProject.mapper;

import com.example.testProject.dto.review.ReviewRequestDTO;
import com.example.testProject.dto.review.ReviewResponseDTO;
import com.example.testProject.entity.Review;

public class ReviewMapper {

    public static Review toEntity(ReviewRequestDTO dto) {
        Review review = new Review();
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
        return review;
    }

    public static ReviewResponseDTO toDTO(Review review) {
        ReviewResponseDTO dto = new ReviewResponseDTO();
        dto.setId(review.getId());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setUserId(review.getUser().getId());
        dto.setBusinessId(review.getBusiness().getId());
        return dto;
    }
}