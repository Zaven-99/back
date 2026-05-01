package com.example.testProject.controller;

import com.example.testProject.dto.business.BusinessDashboardDTO;
import com.example.testProject.service.BookingService;
import com.example.testProject.service.BusinessDashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/business")
public class BusinessController {

    private final BookingService bookingService;
    private final BusinessDashboardService dashboardService;

    public BusinessController(BookingService bookingService,
                              BusinessDashboardService dashboardService) {
        this.bookingService = bookingService;
        this.dashboardService = dashboardService;
    }

    @GetMapping("/bookings")
    public ResponseEntity<?> getBookings() {
        return ResponseEntity.ok(bookingService.getBusinessBookings());
    }

    @PatchMapping("/bookings/{id}/confirm")
    public ResponseEntity<?> confirm(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.confirmBooking(id));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<BusinessDashboardDTO> dashboard() {
        return ResponseEntity.ok(dashboardService.getDashboard());
    }
}