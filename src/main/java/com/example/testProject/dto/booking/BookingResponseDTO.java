package com.example.testProject.dto.booking;

import com.example.testProject.enums.BookingStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookingResponseDTO {
    private Long id;
    private Long userId;
    private Long businessId;
    private Long employeeId;
    private LocalDateTime dateTime;
    private Double price;
    private BookingStatus status;
}