package com.example.testProject.service.availability;

import com.example.testProject.dto.availability.AvailabilityRequestDTO;
import com.example.testProject.dto.availability.TimeSlotDTO;

import java.util.List;

public interface AvailabilityService {
    List<TimeSlotDTO> getAvailableSlots(AvailabilityRequestDTO request);

    boolean isAvailable(AvailabilityRequestDTO request);

}