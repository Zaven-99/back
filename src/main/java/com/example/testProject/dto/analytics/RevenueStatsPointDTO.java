package com.example.testProject.dto.analytics;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RevenueStatsPointDTO {
    private LocalDate date;
    private Double revenue;
}