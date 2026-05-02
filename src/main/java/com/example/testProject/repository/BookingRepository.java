package com.example.testProject.repository;

import com.example.testProject.dto.availability.AvailabilityRequestDTO;
import com.example.testProject.entity.Booking;
import com.example.testProject.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUser_Id(Long userId);

    List<Booking> findByBusiness_Id(Long businessId);

    boolean existsByEmployee_IdAndDateTime(Long employeeId, LocalDateTime dateTime);

    Long countByBusiness_IdAndStatus(Long businessId, BookingStatus status);

    @Query("SELECT SUM(b.price) FROM Booking b WHERE b.business.id = :id AND b.status = 'CONFIRMED'")
    Double getRevenue(@Param("id") Long id);

    @Query("SELECT COUNT(DISTINCT b.user.id) FROM Booking b WHERE b.business.id = :id")
    Long getUniqueClients(@Param("id") Long id);

    @Query("""
            SELECT FUNCTION('DATE', b.dateTime) as date, COUNT(b)
            FROM Booking b
            WHERE b.business.id = :businessId
            GROUP BY FUNCTION('DATE', b.dateTime)
            ORDER BY date
            """)
    List<Object[]> getBookingsPerDay(@Param("businessId") Long businessId);

    @Query("""
            SELECT FUNCTION('DATE', b.dateTime), SUM(b.price)
            FROM Booking b
            WHERE b.business.id = :businessId AND b.status = 'CONFIRMED'
            GROUP BY FUNCTION('DATE', b.dateTime)
            ORDER BY 1
            """)
    List<Object[]> getRevenuePerDay(@Param("businessId") Long businessId);

    List<Booking> findByEmployee_IdAndDateTimeBetween(
            Long employeeId,
            LocalDateTime start,
            LocalDateTime end
    );

    List<Booking> findByBusiness_IdAndDateTimeBetween(
            Long businessId,
            LocalDateTime start,
            LocalDateTime end
    );

    @Query("""
            SELECT FUNCTION('DATE', b.dateTime) as date, COUNT(b)
            FROM Booking b
            WHERE b.business.id = :businessId
            AND b.dateTime BETWEEN :from AND :to
            GROUP BY FUNCTION('DATE', b.dateTime)
            ORDER BY date
            """)
    List<Object[]> getBookingStats(Long businessId, LocalDateTime from, LocalDateTime to);

    @Query("""
            SELECT FUNCTION('DATE', b.dateTime), SUM(b.price)
            FROM Booking b
            WHERE b.business.id = :businessId
            AND b.status = 'COMPLETED'
            AND b.dateTime BETWEEN :from AND :to
            GROUP BY FUNCTION('DATE', b.dateTime)
            ORDER BY 1
            """)
    List<Object[]> getRevenueStats(Long businessId, LocalDateTime from, LocalDateTime to);

 }