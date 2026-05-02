package com.example.testProject.dto.booking;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BookingFilterRequestDTO {

    private Long businessId;
    private Long employeeId;
    private String status;

    private Double minPrice;
    private Double maxPrice;

    private LocalDate fromDate;
    private LocalDate toDate;
}