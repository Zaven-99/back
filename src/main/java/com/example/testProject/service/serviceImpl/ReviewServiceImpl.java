package com.example.testProject.service.serviceImpl;

import com.example.testProject.dto.review.ReviewRequestDTO;
import com.example.testProject.dto.review.ReviewResponseDTO;
import com.example.testProject.entity.Business;
import com.example.testProject.entity.Review;
import com.example.testProject.entity.User;
import com.example.testProject.mapper.ReviewMapper;
import com.example.testProject.repository.BusinessRepository;
import com.example.testProject.repository.ReviewRepository;
import com.example.testProject.repository.UserRepository;
import com.example.testProject.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final BusinessRepository businessRepository;
    private final ReviewMapper reviewMapper;

    @Override
    public ReviewResponseDTO create(ReviewRequestDTO dto) {

        // 🔐 user берётся только из JWT
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Business business = businessRepository.findById(dto.getBusinessId())
                .orElseThrow(() -> new RuntimeException("Business not found"));

        Review review = new Review();
        review.setUser(user);
        review.setBusiness(business);
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());

        Review saved = reviewRepository.save(review);

        return reviewMapper.toDTO(saved);
    }

    @Override
    public ReviewResponseDTO getById(Long id) {
        return reviewRepository.findById(id)
                .map(reviewMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Review not found"));
    }

    @Override
    public List<ReviewResponseDTO> getAll() {
        return reviewRepository.findAll()
                .stream()
                .map(reviewMapper::toDTO)
                .toList();
    }

    @Override
    public List<ReviewResponseDTO> getByBusiness(Long businessId) {
        return reviewRepository.findByBusiness_Id(businessId)
                .stream()
                .map(reviewMapper::toDTO)
                .toList();
    }

    @Override
    public List<ReviewResponseDTO> getByUser(Long userId) {
        return reviewRepository.findByUser_Id(userId)
                .stream()
                .map(reviewMapper::toDTO)
                .toList();
    }

    @Override
    public ReviewResponseDTO update(Long id, ReviewRequestDTO dto) {

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        Business business = businessRepository.findById(dto.getBusinessId())
                .orElseThrow(() -> new RuntimeException("Business not found"));

        // ❗ user НЕ меняем (review принадлежит автору навсегда)
        review.setBusiness(business);
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());

        Review saved = reviewRepository.save(review);

        return reviewMapper.toDTO(saved);
    }

    @Override
    public void delete(Long id) {
        reviewRepository.deleteById(id);
    }
}