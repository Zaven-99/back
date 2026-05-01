package com.example.testProject.service;


import com.example.testProject.dto.booking.BookingRequestDTO;
import com.example.testProject.dto.booking.BookingResponseDTO;

import java.util.List;

public interface BookingService {

    BookingResponseDTO createBooking(BookingRequestDTO dto);

    List<BookingResponseDTO> getBookingsByUser(Long userId);

    List<BookingResponseDTO> getBookingsByBusiness(Long businessId);

    List<BookingResponseDTO> getBusinessBookings();

    BookingResponseDTO confirmBooking(Long bookingId);

    BookingResponseDTO getById(Long id);

    List<BookingResponseDTO> getAll();

    BookingResponseDTO update(Long id, BookingRequestDTO dto);

    void delete(Long id);

}
