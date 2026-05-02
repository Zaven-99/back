package com.example.testProject.service.availability;

import com.example.testProject.dto.availability.TimeSlotDTO;

import java.time.LocalDate;
import java.util.List;

public interface SmartSuggestionService {

    List<TimeSlotDTO> suggest(Long businessId, Long serviceId, LocalDate fromDate);
}