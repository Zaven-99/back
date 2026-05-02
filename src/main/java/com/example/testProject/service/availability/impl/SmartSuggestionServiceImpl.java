package com.example.testProject.service.availability.impl;

import com.example.testProject.dto.availability.TimeSlotDTO;
import com.example.testProject.entity.BusinessSchedule;
import com.example.testProject.entity.Booking;
import com.example.testProject.entity.ServiceEntity;
import com.example.testProject.repository.BookingRepository;
import com.example.testProject.repository.BusinessScheduleRepository;
import com.example.testProject.repository.ServiceRepository;
import com.example.testProject.service.availability.SmartSuggestionService;
import lombok.RequiredArgsConstructor;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class SmartSuggestionServiceImpl implements SmartSuggestionService {

    private final BookingRepository bookingRepository;
    private final BusinessScheduleRepository scheduleRepository;
    private final ServiceRepository serviceRepository;

    private static final int STEP_MINUTES = 15;

    @Override
    public List<TimeSlotDTO> suggest(Long businessId, Long serviceId, LocalDate fromDate) {

        ServiceEntity service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        int duration = service.getDurationMinutes();
        int buffer = service.getBufferMinutes() != null ? service.getBufferMinutes() : 0;
        int totalMinutes = duration + buffer;

        List<TimeSlotDTO> result = new ArrayList<>();

        for (int dayOffset = 0; dayOffset < 7; dayOffset++) {

            LocalDate date = fromDate.plusDays(dayOffset);
            DayOfWeek dayOfWeek = date.getDayOfWeek();

            BusinessSchedule schedule = scheduleRepository
                    .findByBusiness_IdAndDayOfWeek(businessId, dayOfWeek)
                    .orElse(null);

            if (schedule == null || schedule.isDayOff()) {
                continue;
            }

            LocalDateTime dayStart = LocalDateTime.of(date, schedule.getOpenTime());
            LocalDateTime dayEnd = LocalDateTime.of(date, schedule.getCloseTime());

            List<Booking> bookings = bookingRepository
                    .findByBusiness_IdAndDateTimeBetween(businessId, dayStart, dayEnd);

            for (LocalDateTime slot = dayStart;
                 slot.isBefore(dayEnd);
                 slot = slot.plusMinutes(STEP_MINUTES)) {

                final LocalDateTime currentStart = slot;
                final LocalDateTime currentEnd = slot.plusMinutes(totalMinutes);

                if (currentEnd.isAfter(dayEnd)) break;

                boolean isFree = bookings.stream().noneMatch(b -> {

                    int bDuration = b.getService().getDurationMinutes();
                    int bBuffer = b.getService().getBufferMinutes() != null
                            ? b.getService().getBufferMinutes()
                            : 0;

                    LocalDateTime bStart = b.getDateTime();
                    LocalDateTime bEnd = b.getDateTime().plusMinutes(bDuration + bBuffer);

                    return !(bEnd.isBefore(currentStart) || bStart.isAfter(currentEnd));
                });

                if (isFree) {
                    result.add(new TimeSlotDTO(
                            currentStart.toLocalTime(),
                            currentEnd.toLocalTime(),
                            true
                    ));
                }

                if (result.size() >= 10) {
                    return result;
                }
            }
        }

        return result;
    }
}