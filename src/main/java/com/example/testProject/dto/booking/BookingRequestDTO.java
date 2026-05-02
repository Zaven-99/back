package com.example.testProject.dto.booking;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookingRequestDTO {
    private Long businessId;
    private Long serviceId;
    private Long employeeId;

    private LocalDateTime dateTime;
    private Double price;
}