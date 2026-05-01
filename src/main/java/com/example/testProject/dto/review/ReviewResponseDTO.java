package com.example.testProject.dto.review;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewResponseDTO {
    private Long id;
    private int rating;
    private String comment;
    private Long userId;
    private Long businessId;
}