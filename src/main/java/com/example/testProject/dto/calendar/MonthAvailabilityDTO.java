package com.example.testProject.dto.calendar;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MonthAvailabilityDTO {

    private int year;
    private int month;
    private Map<LocalDate, Boolean> days;
}