package com.example.testProject.controller.availability;

import com.example.testProject.dto.availability.AvailabilityRequestDTO;
import com.example.testProject.dto.availability.TimeSlotDTO;
import com.example.testProject.service.availability.AvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/availability")
@RequiredArgsConstructor
public class AvailabilityCrudController {

    private final AvailabilityService availabilityService;

    @PostMapping
    public List<TimeSlotDTO> getSlots(@RequestBody AvailabilityRequestDTO request) {
        return availabilityService.getAvailableSlots(request);
    }
}