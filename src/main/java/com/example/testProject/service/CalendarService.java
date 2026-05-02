package com.example.testProject.service;

 import com.example.testProject.dto.calendar.CalendarSlotDTO;
 import com.example.testProject.dto.calendar.MonthAvailabilityDTO;

 import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface CalendarService {

    MonthAvailabilityDTO getMonthAvailability(Long businessId, int year, int month);


    List<CalendarSlotDTO> getDaySlots(Long businessId, Long serviceId, LocalDate date);
}