package com.example.testProject.dto.booking;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookingRequestDTO {
    @NotNull
    private Long businessId;
    @NotNull
    private Long employeeId;
    @NotNull
    private LocalDateTime dateTime;
    @NotNull
    private Double price;
}