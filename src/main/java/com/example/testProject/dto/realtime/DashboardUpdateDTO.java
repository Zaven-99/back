package com.example.testProject.dto.realtime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DashboardUpdateDTO {
    private Long businessId;
    private String type; // BOOKING_CREATED / REVENUE_UPDATED
    private Object payload;
}