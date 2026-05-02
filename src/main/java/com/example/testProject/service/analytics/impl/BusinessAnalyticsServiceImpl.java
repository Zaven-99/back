package com.example.testProject.service.analytics.impl;

import com.example.testProject.dto.analytics.BookingStatsPointDTO;
import com.example.testProject.dto.analytics.RevenueStatsPointDTO;
import com.example.testProject.repository.BookingRepository;
import com.example.testProject.repository.ReviewRepository;
import com.example.testProject.service.analytics.BusinessAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BusinessAnalyticsServiceImpl implements BusinessAnalyticsService {

    private final BookingRepository bookingRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public List<BookingStatsPointDTO> getBookingsChart(Long businessId,
                                                       LocalDate from,
                                                       LocalDate to) {

        List<Object[]> raw = bookingRepository
                .getBookingStats(businessId,
                        from.atStartOfDay(),
                        to.atTime(23, 59, 59));

        List<BookingStatsPointDTO> result = new ArrayList<>();

        for (Object[] row : raw) {
            BookingStatsPointDTO dto = new BookingStatsPointDTO();
            dto.setDate(((java.sql.Date) row[0]).toLocalDate());
            dto.setCount((Long) row[1]);
            result.add(dto);
        }

        return result;
    }

    @Override
    public List<RevenueStatsPointDTO> getRevenueChart(Long businessId,
                                                      LocalDate from,
                                                      LocalDate to) {

        List<Object[]> raw = bookingRepository
                .getRevenueStats(businessId,
                        from.atStartOfDay(),
                        to.atTime(23, 59, 59));

        List<RevenueStatsPointDTO> result = new ArrayList<>();

        for (Object[] row : raw) {
            RevenueStatsPointDTO dto = new RevenueStatsPointDTO();
            dto.setDate(((java.sql.Date) row[0]).toLocalDate());
            dto.setRevenue((Double) row[1]);
            result.add(dto);
        }

        return result;
    }
}