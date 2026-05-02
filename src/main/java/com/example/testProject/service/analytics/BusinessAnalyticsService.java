package com.example.testProject.service.analytics;

import com.example.testProject.dto.analytics.BookingStatsPointDTO;
import com.example.testProject.dto.analytics.RevenueStatsPointDTO;

import java.time.LocalDate;
import java.util.List;

public interface BusinessAnalyticsService {

    List<BookingStatsPointDTO> getBookingsChart(Long businessId,
                                                LocalDate from,
                                                LocalDate to);

    List<RevenueStatsPointDTO> getRevenueChart(Long businessId,
                                               LocalDate from,
                                               LocalDate to);
}