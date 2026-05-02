package com.example.testProject.controller;

import com.example.testProject.dto.calendar.CalendarSlotDTO;
import com.example.testProject.dto.calendar.MonthAvailabilityDTO;
import com.example.testProject.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/calendar")
@RequiredArgsConstructor
public class CalendarCrudController {

    private final CalendarService calendarService;

    @GetMapping("/month")
    public MonthAvailabilityDTO month(
            @RequestParam Long businessId,
            @RequestParam int year,
            @RequestParam int month
    ) {
        return calendarService.getMonthAvailability(businessId, year, month);
    }

    @GetMapping("/slots")
    public List<CalendarSlotDTO> slots(
            @RequestParam Long businessId,
            @RequestParam Long serviceId,
            @RequestParam LocalDate date
    ) {
        return calendarService.getDaySlots(businessId, serviceId, date);
    }
}