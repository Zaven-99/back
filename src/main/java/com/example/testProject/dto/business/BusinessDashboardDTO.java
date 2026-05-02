package com.example.testProject.dto.business;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessDashboardDTO {
    private String businessName;
    private Long pendingBookings;
    private Double revenueToday;
    private Long totalClients;

    private Double averageRating;
    private Long reviewsCount;
}