package com.example.testProject.dto.booking;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookingRequestDTO {
    private Long userId;
    private Long businessId;
    private LocalDateTime dateTime;
}