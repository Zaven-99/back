package com.example.testProject.dto.review;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequestDTO {
    private Long userId;
    private Long businessId;
    private int rating;
    private String comment;
}