package com.example.testProject.service;

import com.example.testProject.dto.business.BusinessDashboardDTO;
import com.example.testProject.entity.Business;
import com.example.testProject.entity.User;
import com.example.testProject.enums.BookingStatus;
import com.example.testProject.repository.BookingRepository;
import com.example.testProject.repository.BusinessRepository;
import com.example.testProject.security.CustomUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class BusinessDashboardService {

    private final BookingRepository bookingRepository;
    private final BusinessRepository businessRepository;

    public BusinessDashboardService(BookingRepository bookingRepository,
                                    BusinessRepository businessRepository) {
        this.bookingRepository = bookingRepository;
        this.businessRepository = businessRepository;
    }

    public BusinessDashboardDTO getDashboard() {

        User user = ((CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUser();

        Business business = businessRepository.findByOwnerId(user.getId())
                .orElseThrow(() -> new RuntimeException("Business not found"));

        Long pendingBookings = bookingRepository
                .countByBusiness_IdAndStatus(business.getId(), BookingStatus.PENDING);

        Double revenue = bookingRepository.getRevenue(business.getId());
        if (revenue == null) revenue = 0.0;

        Long clients = bookingRepository.getUniqueClients(business.getId());

        BusinessDashboardDTO dto = new BusinessDashboardDTO();
        dto.setBusinessName(business.getName());
        dto.setTodayBookings(pendingBookings);
        dto.setRevenueToday(revenue);
        dto.setAverageRating(0.0); // позже через Review
        dto.setTotalClients(clients);
        dto.setPendingBookings(pendingBookings);

        return dto;
    }
}