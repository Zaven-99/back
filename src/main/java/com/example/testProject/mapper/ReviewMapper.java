package com.example.testProject.mapper;

import com.example.testProject.dto.review.ReviewRequestDTO;
import com.example.testProject.dto.review.ReviewResponseDTO;
import com.example.testProject.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface ReviewMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "business", ignore = true)
    Review toEntity(ReviewRequestDTO dto);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "businessId", source = "business.id")
    ReviewResponseDTO toDTO(Review review);
}