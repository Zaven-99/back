package com.example.testProject.dto.business;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessDashboardDTO {
    private String businessName;
    private Long todayBookings;
    private Double revenueToday;
    private Double averageRating;
    private Long totalClients;
    private Long pendingBookings;
}