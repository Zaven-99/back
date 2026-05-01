package com.example.testProject.repository;

import com.example.testProject.entity.Booking;
import com.example.testProject.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUser_Id(Long userId);

    List<Booking> findByBusiness_Id(Long businessId);

    Long countByBusiness_IdAndStatus(Long businessId, BookingStatus status);

    @Query("SELECT SUM(b.price) FROM Booking b WHERE b.business.id = :id AND b.status = 'CONFIRMED'")
    Double getRevenue(@Param("id") Long id);

    @Query("SELECT COUNT(DISTINCT b.user.id) FROM Booking b WHERE b.business.id = :id")
    Long getUniqueClients(@Param("id") Long id);
}