package com.example.testProject.service.serviceImpl;

import com.example.testProject.dto.calendar.CalendarSlotDTO;
import com.example.testProject.dto.calendar.MonthAvailabilityDTO;
import com.example.testProject.entity.Booking;
import com.example.testProject.entity.BusinessSchedule;
import com.example.testProject.entity.ServiceEntity;
import com.example.testProject.repository.BookingRepository;
import com.example.testProject.repository.BusinessScheduleRepository;
import com.example.testProject.repository.ServiceRepository;
import com.example.testProject.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CalendarServiceImpl implements CalendarService {

    private final BookingRepository bookingRepository;
    private final BusinessScheduleRepository scheduleRepository;
    private final ServiceRepository serviceRepository;

    private static final int STEP_MINUTES = 15;

    // ================= MONTH =================
    @Override
    public MonthAvailabilityDTO getMonthAvailability(Long businessId, int year, int month) {

        YearMonth yearMonth = YearMonth.of(year, month);
        Map<LocalDate, Boolean> days = new LinkedHashMap<>();

        for (int i = 1; i <= yearMonth.lengthOfMonth(); i++) {
            LocalDate date = yearMonth.atDay(i);
            days.put(date, hasAvailableSlots(businessId, date));
        }

        MonthAvailabilityDTO dto = new MonthAvailabilityDTO();
        dto.setYear(year);
        dto.setMonth(month);
        dto.setDays(days);

        return dto;
    }

    // ================= DAY =================
    @Override
    public List<CalendarSlotDTO> getDaySlots(Long businessId, Long serviceId, LocalDate date) {

        BusinessSchedule schedule = scheduleRepository
                .findByBusiness_IdAndDayOfWeek(businessId, date.getDayOfWeek())
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        if (schedule.isDayOff()) {
            return List.of();
        }

        ServiceEntity service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        int duration = service.getDurationMinutes();
        int buffer = service.getBufferMinutes() != null ? service.getBufferMinutes() : 0;
        int totalMinutes = duration + buffer;

        LocalDateTime start = LocalDateTime.of(date, schedule.getOpenTime());
        LocalDateTime end = LocalDateTime.of(date, schedule.getCloseTime());

        List<Booking> bookings = bookingRepository
                .findByBusiness_IdAndDateTimeBetween(businessId, start, end);

        List<CalendarSlotDTO> slots = new ArrayList<>();

        for (LocalDateTime slot = start; slot.isBefore(end); slot = slot.plusMinutes(STEP_MINUTES)) {

            final LocalDateTime currentStart = slot;
            final LocalDateTime currentEnd = slot.plusMinutes(totalMinutes);

            if (currentEnd.isAfter(end)) break;

            boolean taken = bookings.stream().anyMatch(b -> {

                int bDuration = b.getService().getDurationMinutes();
                int bBuffer = b.getService().getBufferMinutes() != null
                        ? b.getService().getBufferMinutes()
                        : 0;

                LocalDateTime bStart = b.getDateTime();
                LocalDateTime bEnd = bStart.plusMinutes(bDuration + bBuffer);

                return !(bEnd.isBefore(currentStart) || bStart.isAfter(currentEnd));
            });

            slots.add(new CalendarSlotDTO(
                    currentStart.toLocalTime(),
                    currentEnd.toLocalTime(),
                    !taken
            ));
        }

        return slots;
    }

    // ================= HELPER =================
    private boolean hasAvailableSlots(Long businessId, LocalDate date) {

        BusinessSchedule schedule = scheduleRepository
                .findByBusiness_IdAndDayOfWeek(businessId, date.getDayOfWeek())
                .orElse(null);

        if (schedule == null || schedule.isDayOff()) {
            return false;
        }

        LocalDateTime start = LocalDateTime.of(date, schedule.getOpenTime());
        LocalDateTime end = LocalDateTime.of(date, schedule.getCloseTime());

        List<Booking> bookings = bookingRepository
                .findByBusiness_IdAndDateTimeBetween(businessId, start, end);

        long totalSlots = Duration.between(start, end).toMinutes() / STEP_MINUTES;

        return bookings.size() < totalSlots;
    }
}