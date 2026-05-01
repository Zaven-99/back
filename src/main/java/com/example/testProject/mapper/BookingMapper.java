package com.example.testProject.mapper;

 import com.example.testProject.dto.booking.BookingResponseDTO;
 import com.example.testProject.entity.Booking;

public class BookingMapper {

    public static BookingResponseDTO toDTO(Booking booking) {
        BookingResponseDTO dto = new BookingResponseDTO();
        dto.setId(booking.getId());
        dto.setDateTime(booking.getDateTime());
        dto.setStatus(booking.getStatus());

        dto.setUserId(booking.getUser().getId());
        dto.setBusinessId(booking.getBusiness().getId());

        return dto;
    }
}