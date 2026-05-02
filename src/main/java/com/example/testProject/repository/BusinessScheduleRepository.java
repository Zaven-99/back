package com.example.testProject.repository;

import com.example.testProject.entity.BusinessSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.Optional;

public interface BusinessScheduleRepository extends JpaRepository<BusinessSchedule, Long> {

    Optional<BusinessSchedule> findByBusiness_IdAndDayOfWeek(
            Long businessId,
            DayOfWeek dayOfWeek
    );
}