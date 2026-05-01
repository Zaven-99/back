package com.example.testProject.controller;

  import com.example.testProject.dto.booking.BookingRequestDTO;
  import com.example.testProject.dto.booking.BookingResponseDTO;
  import com.example.testProject.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // 📌 создать запись
    @PostMapping
    public ResponseEntity<BookingResponseDTO> create(@RequestBody BookingRequestDTO dto) {
        return ResponseEntity.ok(bookingService.createBooking(dto));
    }

    // 📌 все записи пользователя
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookingResponseDTO>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(bookingService.getBookingsByUser(userId));
    }

    // 📌 все записи бизнеса
    @GetMapping("/business/{businessId}")
    public ResponseEntity<List<BookingResponseDTO>> getByBusiness(@PathVariable Long businessId) {
        return ResponseEntity.ok(bookingService.getBookingsByBusiness(businessId));
    }
}