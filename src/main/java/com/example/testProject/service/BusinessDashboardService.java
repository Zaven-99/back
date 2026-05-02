package com.example.testProject.service;

import com.example.testProject.dto.business.BusinessDashboardDTO;
import com.example.testProject.entity.Business;
import com.example.testProject.entity.User;
import com.example.testProject.enums.BookingStatus;
import com.example.testProject.repository.BookingRepository;
import com.example.testProject.repository.BusinessRepository;
import com.example.testProject.repository.ReviewRepository;
import com.example.testProject.security.SecurityUtils;
import org.springframework.stereotype.Service;

@Service
public class BusinessDashboardService {

    private final BookingRepository bookingRepository;
    private final BusinessRepository businessRepository;
    private final ReviewRepository reviewRepository;

    public BusinessDashboardService(
            BookingRepository bookingRepository,
            BusinessRepository businessRepository,
            ReviewRepository reviewRepository
    ) {
        this.bookingRepository = bookingRepository;
        this.businessRepository = businessRepository;
        this.reviewRepository = reviewRepository;
    }

    public BusinessDashboardDTO getDashboard() {

        User user = SecurityUtils.currentUser();

        Business business = businessRepository.findByOwnerId(user.getId())
                .orElseThrow(() -> new RuntimeException("Business not found"));

        Long pendingBookings = bookingRepository
                .countByBusiness_IdAndStatus(business.getId(), BookingStatus.PENDING);

        Double revenue = bookingRepository.getRevenue(business.getId());
        if (revenue == null) revenue = 0.0;

        Long clients = bookingRepository.getUniqueClients(business.getId());

        Double avgRating = reviewRepository.getAverageRating(business.getId());
        long reviewsCount = reviewRepository.countByBusiness_Id(business.getId());

        BusinessDashboardDTO dto = new BusinessDashboardDTO();
        dto.setBusinessName(business.getName());
        dto.setPendingBookings(pendingBookings);
        dto.setRevenueToday(revenue);
        dto.setTotalClients(clients);
        dto.setAverageRating(avgRating);
        dto.setReviewsCount(reviewsCount);

        return dto;
    }
}