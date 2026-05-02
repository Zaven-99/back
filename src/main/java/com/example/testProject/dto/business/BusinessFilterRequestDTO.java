package com.example.testProject.dto.business;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessFilterRequestDTO {

    private Double latitude;
    private Double longitude;

    private Double maxDistanceKm;

    private Double minRating;

    private Double minPrice;
    private Double maxPrice;

    private String search; // название / категория


}