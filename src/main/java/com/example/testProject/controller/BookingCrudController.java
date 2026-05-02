package com.example.testProject.controller;

import com.example.testProject.dto.booking.BookingFilterRequestDTO;
import com.example.testProject.dto.booking.BookingRequestDTO;
import com.example.testProject.dto.booking.BookingResponseDTO;
import com.example.testProject.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingCrudController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponseDTO> create(@RequestBody BookingRequestDTO dto) {
        return ResponseEntity.ok(bookingService.createBooking(dto));
    }

    @GetMapping("/my")
    public ResponseEntity<List<BookingResponseDTO>> my() {
        return ResponseEntity.ok(bookingService.getMyBookings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getMyBookingById(id));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        bookingService.cancel(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookingService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<BookingResponseDTO> confirm(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.confirm(id));
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<BookingResponseDTO> complete(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.complete(id));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<BookingResponseDTO>> filter(
            @RequestParam(required = false) Long businessId,
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to
    ) {

        BookingFilterRequestDTO request = new BookingFilterRequestDTO();
        request.setBusinessId(businessId);
        request.setEmployeeId(employeeId);
        request.setStatus(status);
        request.setMinPrice(minPrice);
        request.setMaxPrice(maxPrice);
        request.setFromDate(from);
        request.setToDate(to);

        return ResponseEntity.ok(
                bookingService.filter(request)
        );
    }
}