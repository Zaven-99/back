package com.example.testProject.controller;

import com.example.testProject.dto.review.ReviewFilterRequestDTO;
import com.example.testProject.dto.review.ReviewRequestDTO;
import com.example.testProject.dto.review.ReviewResponseDTO;
import com.example.testProject.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewCrudController {

    private final ReviewService reviewService;

    // ================= CREATE =================
    @PostMapping
    public ResponseEntity<ReviewResponseDTO> create(@RequestBody ReviewRequestDTO dto) {
        return ResponseEntity.ok(reviewService.create(dto));
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getById(id));
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reviewService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ================= MAIN SEARCH (FILTER + PAGINATION) =================
    @GetMapping
    public ResponseEntity<List<ReviewResponseDTO>> getAll(
            @RequestParam(required = false) Long businessId,
            @RequestParam(required = false) Integer minRating,
            @RequestParam(required = false) Integer maxRating,
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate,
            @RequestParam(required = false, defaultValue = "newest") String sortBy,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size
    ) {

        ReviewFilterRequestDTO request = new ReviewFilterRequestDTO();
        request.setBusinessId(businessId);
        request.setMinRating(minRating);
        request.setMaxRating(maxRating);
        request.setFromDate(fromDate);
        request.setToDate(toDate);
        request.setSortBy(sortBy);

        return ResponseEntity.ok(
                reviewService.filter(request, page, size)
        );
    }
}