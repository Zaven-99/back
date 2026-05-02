package com.example.testProject.dto.review;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ReviewFilterRequestDTO {

    private Long businessId;

    private Integer minRating;
    private Integer maxRating;

    private LocalDate fromDate;
    private LocalDate toDate;

    private String sortBy; // "newest" | "oldest"

}