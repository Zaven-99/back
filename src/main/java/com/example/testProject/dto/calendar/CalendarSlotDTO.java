package com.example.testProject.dto.calendar;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CalendarSlotDTO {
    private LocalTime start;
    private LocalTime end;
    private boolean available;
}