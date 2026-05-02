package com.example.testProject.dto.analytics;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessAnalyticsDTO {

    private Long totalBookings;
    private Double revenue;
    private Long uniqueClients;
    private Double averageRating;

    private Long pendingBookings;
    private Long confirmedBookings;
    private Long completedBookings;
    private Long canceledBookings;
}