package com.example.testProject.service;

 import com.example.testProject.dto.booking.BookingFilterRequestDTO;
 import com.example.testProject.dto.booking.BookingRequestDTO;
import com.example.testProject.dto.booking.BookingResponseDTO;

import java.util.List;

public interface BookingService {

    BookingResponseDTO createBooking(BookingRequestDTO request);

    List<BookingResponseDTO> getMyBookings();

    List<BookingResponseDTO> getBusinessBookings();

    BookingResponseDTO getMyBookingById(Long id);

    BookingResponseDTO update(Long id, BookingRequestDTO request);

    void cancel(Long id);

    void delete(Long id);

    BookingResponseDTO confirm(Long id);

    BookingResponseDTO complete(Long id);

    List<BookingResponseDTO> filter(BookingFilterRequestDTO request);
}