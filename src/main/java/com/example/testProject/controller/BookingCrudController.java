package com.example.testProject.controller;

import com.example.testProject.dto.booking.BookingRequestDTO;
import com.example.testProject.dto.booking.BookingResponseDTO;
import com.example.testProject.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingCrudController {

    private final BookingService bookingService;

    // CREATE booking (user берётся из JWT)
    @PostMapping
    public ResponseEntity<BookingResponseDTO> create(
            @RequestBody  @Valid BookingRequestDTO dto
    ) {
        return ResponseEntity.ok(bookingService.createBooking(dto));
    }

    // GET by id
    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getById(id));
    }

    // GET all or filtered
    @GetMapping
    public ResponseEntity<List<BookingResponseDTO>> getAll(
            @RequestParam(required = false) Long businessId
    ) {
        if (businessId != null) {
            return ResponseEntity.ok(bookingService.getBookingsByBusiness(businessId));
        }
        return ResponseEntity.ok(bookingService.getAll());
    }

    // UPDATE booking
    @PutMapping("/{id}")
    public ResponseEntity<BookingResponseDTO> update(
            @PathVariable Long id,
            @RequestBody BookingRequestDTO dto
    ) {
        return ResponseEntity.ok(bookingService.update(id, dto));
    }

    // DELETE booking
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookingService.delete(id);
        return ResponseEntity.noContent().build();
    }
}