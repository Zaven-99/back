package com.example.testProject.dto.availability;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;

@Data
@AllArgsConstructor
public class TimeSlotDTO {
    private LocalTime start;
    private LocalTime end;
    private boolean available;
}