package com.example.testProject.service.availability.impl;

import com.example.testProject.dto.availability.AvailabilityRequestDTO;
import com.example.testProject.dto.availability.TimeSlotDTO;
import com.example.testProject.entity.Booking;
import com.example.testProject.entity.BusinessSchedule;
import com.example.testProject.entity.ServiceEntity;
import com.example.testProject.repository.BookingRepository;
import com.example.testProject.repository.BusinessScheduleRepository;
import com.example.testProject.repository.ServiceRepository;
import com.example.testProject.service.availability.AvailabilityService;
import lombok.RequiredArgsConstructor;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class AvailabilityServiceImpl implements AvailabilityService {

    private final BookingRepository bookingRepository;
    private final BusinessScheduleRepository businessScheduleRepository;
    private final ServiceRepository serviceRepository;

    private static final int STEP_MINUTES = 15;

    @Override
    public List<TimeSlotDTO> getAvailableSlots(AvailabilityRequestDTO request) {

        LocalDate date = request.getDate();
        DayOfWeek dayOfWeek = date.getDayOfWeek();

        // 📌 1. schedule (рабочее время бизнеса)
        BusinessSchedule schedule = businessScheduleRepository
                .findByBusiness_IdAndDayOfWeek(request.getBusinessId(), dayOfWeek)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        if (schedule.isDayOff()) {
            return List.of();
        }

        // 📌 2. service (важно для duration)
        ServiceEntity service = serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new RuntimeException("Service not found"));

        int duration = service.getDurationMinutes();
        int buffer = service.getBufferMinutes() != null ? service.getBufferMinutes() : 0;

        int totalBlockMinutes = duration + buffer;

        LocalDateTime startDateTime = LocalDateTime.of(date, schedule.getOpenTime());
        LocalDateTime endDateTime = LocalDateTime.of(date, schedule.getCloseTime());

        // 📌 3. bookings за день
        List<Booking> bookings = bookingRepository
                .findByBusiness_IdAndDateTimeBetween(
                        request.getBusinessId(),
                        startDateTime,
                        endDateTime
                );

        List<TimeSlotDTO> result = new ArrayList<>();

        // 📌 4. генерация слотов
        for (LocalDateTime slotStart = startDateTime;
             slotStart.isBefore(endDateTime);
             slotStart = slotStart.plusMinutes(STEP_MINUTES)) {

            final LocalDateTime currentStart = slotStart;
            final LocalDateTime currentEnd = slotStart.plusMinutes(totalBlockMinutes);

            if (currentEnd.isAfter(endDateTime)) break;

            boolean isTaken = bookings.stream().anyMatch(b -> {

                int bDuration = b.getService().getDurationMinutes();
                int bBuffer = b.getService().getBufferMinutes() != null
                        ? b.getService().getBufferMinutes()
                        : 0;

                LocalDateTime bookingStart = b.getDateTime();
                LocalDateTime bookingEnd = b.getDateTime()
                        .plusMinutes(bDuration + bBuffer);

                return !(bookingEnd.isBefore(currentStart) ||
                        bookingStart.isAfter(currentEnd));
            });

            result.add(new TimeSlotDTO(
                    currentStart.toLocalTime(),
                    currentEnd.toLocalTime(),
                    !isTaken
            ));
        }

        return result;
    }

    @Override
    public boolean isAvailable(AvailabilityRequestDTO request) {

        LocalDateTime dateTime = request.getDate().atStartOfDay();
        DayOfWeek dayOfWeek = request.getDate().getDayOfWeek();

        BusinessSchedule schedule = businessScheduleRepository
                .findByBusiness_IdAndDayOfWeek(request.getBusinessId(), dayOfWeek)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        if (schedule.isDayOff()) {
            return false;
        }

        ServiceEntity service = serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new RuntimeException("Service not found"));

        int duration = service.getDurationMinutes();
        int buffer = service.getBufferMinutes() != null ? service.getBufferMinutes() : 0;

        int totalMinutes = duration + buffer;

        LocalDateTime startOfDay = LocalDateTime.of(request.getDate(), schedule.getOpenTime());
        LocalDateTime endOfDay = LocalDateTime.of(request.getDate(), schedule.getCloseTime());

        List<Booking> bookings = bookingRepository.findByBusiness_IdAndDateTimeBetween(
                request.getBusinessId(),
                startOfDay,
                endOfDay
        );

        LocalDateTime requestedStart = request.getDate().atTime(dateTime.toLocalTime());
        LocalDateTime requestedEnd = requestedStart.plusMinutes(totalMinutes);

        // если вне рабочего времени
        if (requestedStart.isBefore(startOfDay) || requestedEnd.isAfter(endOfDay)) {
            return false;
        }

        // проверка пересечений
        return bookings.stream().noneMatch(b -> {

            int bDuration = b.getService().getDurationMinutes();
            int bBuffer = b.getService().getBufferMinutes() != null
                    ? b.getService().getBufferMinutes()
                    : 0;

            LocalDateTime bookingStart = b.getDateTime();
            LocalDateTime bookingEnd = bookingStart.plusMinutes(bDuration + bBuffer);

            return !(bookingEnd.isBefore(requestedStart) ||
                    bookingStart.isAfter(requestedEnd));
        });
    }
}