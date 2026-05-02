package com.example.testProject.dto.availability;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AvailabilityRequestDTO {

    private Long businessId;

    private Long serviceId;   // ❗ ДОБАВИТЬ ЭТО

    private LocalDate date;
}