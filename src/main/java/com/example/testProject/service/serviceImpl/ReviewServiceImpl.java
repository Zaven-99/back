package com.example.testProject.service.serviceImpl;

import com.example.testProject.dto.review.ReviewFilterRequestDTO;
import com.example.testProject.dto.review.ReviewRequestDTO;
import com.example.testProject.dto.review.ReviewResponseDTO;
import com.example.testProject.entity.Business;
import com.example.testProject.entity.Review;
import com.example.testProject.entity.User;
import com.example.testProject.error.NotFoundException;
import com.example.testProject.mapper.ReviewMapper;
import com.example.testProject.repository.BusinessRepository;
import com.example.testProject.repository.ReviewRepository;
import com.example.testProject.security.SecurityUtils;
import com.example.testProject.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final BusinessRepository businessRepository;
    private final ReviewMapper reviewMapper;

    // CREATE
    @Override
    public ReviewResponseDTO create(ReviewRequestDTO dto) {

        User user = SecurityUtils.currentUser();

        Business business = businessRepository.findById(dto.getBusinessId())
                .orElseThrow(() -> new NotFoundException("Business not found"));

        Review review = new Review();
        review.setUser(user);
        review.setBusiness(business);
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());

        return reviewMapper.toDTO(reviewRepository.save(review));
    }

    // GET BY ID
    @Override
    public ReviewResponseDTO getById(Long id) {
        return reviewRepository.findById(id)
                .map(reviewMapper::toDTO)
                .orElseThrow(() -> new NotFoundException("Review not found"));
    }

    // GET ALL
    @Override
    public List<ReviewResponseDTO> getAll() {
        return reviewRepository.findAll()
                .stream()
                .map(reviewMapper::toDTO)
                .toList();
    }

    // GET BY BUSINESS
    @Override
    public List<ReviewResponseDTO> getByBusiness(Long businessId) {
        return reviewRepository.findByBusiness_Id(businessId)
                .stream()
                .map(reviewMapper::toDTO)
                .toList();
    }

    // UPDATE (только автор)
    @Override
    public ReviewResponseDTO update(Long id, ReviewRequestDTO dto) {

        User current = SecurityUtils.currentUser();

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Review not found"));

        if (!review.getUser().getId().equals(current.getId())) {
            throw new RuntimeException("Not your review");
        }

        Business business = businessRepository.findById(dto.getBusinessId())
                .orElseThrow(() -> new NotFoundException("Business not found"));

        review.setBusiness(business);
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());

        return reviewMapper.toDTO(reviewRepository.save(review));
    }

    // DELETE (только автор)
    @Override
    public void delete(Long id) {

        User current = SecurityUtils.currentUser();

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Review not found"));

        if (!review.getUser().getId().equals(current.getId())) {
            throw new RuntimeException("Not your review");
        }

        reviewRepository.delete(review);
    }

    @Override
    public List<ReviewResponseDTO> filter(ReviewFilterRequestDTO request, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return reviewRepository.findAll(pageable)
                .getContent()
                .stream()
                .filter(r -> request.getBusinessId() == null ||
                        r.getBusiness().getId().equals(request.getBusinessId()))
                .filter(r -> request.getMinRating() == null ||
                        r.getRating() >= request.getMinRating())
                .filter(r -> request.getMaxRating() == null ||
                        r.getRating() <= request.getMaxRating())
                .filter(r -> request.getFromDate() == null ||
                        !r.getCreatedAt().toLocalDate().isBefore(request.getFromDate()))
                .filter(r -> request.getToDate() == null ||
                        !r.getCreatedAt().toLocalDate().isAfter(request.getToDate()))
                .sorted((a, b) -> "oldest".equals(request.getSortBy())
                        ? a.getCreatedAt().compareTo(b.getCreatedAt())
                        : b.getCreatedAt().compareTo(a.getCreatedAt()))
                .map(reviewMapper::toDTO)
                .toList();
    }


    public List<ReviewResponseDTO> getAll(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Review> reviews = reviewRepository.findAll(pageable);

        return reviews.getContent()
                .stream()
                .map(reviewMapper::toDTO)
                .toList();
    }


    public List<ReviewResponseDTO> getByBusiness(Long businessId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Review> reviews = reviewRepository.findByBusiness_Id(businessId, pageable);

        return reviews.getContent()
                .stream()
                .map(reviewMapper::toDTO)
                .toList();
    }
}