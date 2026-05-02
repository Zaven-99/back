package com.example.testProject.controller;

import com.example.testProject.dto.analytics.BookingStatsPointDTO;
import com.example.testProject.dto.analytics.RevenueStatsPointDTO;
import com.example.testProject.service.analytics.BusinessAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/analytics/business")
@RequiredArgsConstructor
public class BusinessAnalyticsCrudController {

    private final BusinessAnalyticsService analyticsService;

    @GetMapping("/bookings")
    public List<BookingStatsPointDTO> bookingsChart(
            @RequestParam Long businessId,
            @RequestParam LocalDate from,
            @RequestParam LocalDate to
    ) {
        return analyticsService.getBookingsChart(businessId, from, to);
    }

    @GetMapping("/revenue")
    public List<RevenueStatsPointDTO> revenueChart(
            @RequestParam Long businessId,
            @RequestParam LocalDate from,
            @RequestParam LocalDate to
    ) {
        return analyticsService.getRevenueChart(businessId, from, to);
    }
}