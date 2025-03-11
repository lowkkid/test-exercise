package com.andersenlab.test.andersentest.mapper;

import com.andersenlab.test.andersentest.dto.ReviewDto;
import com.andersenlab.test.andersentest.model.Review;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    ReviewDto toReviewDto(Review review);

    Review toReview(ReviewDto reviewDto);

    default List<ReviewDto> mapReviews(List<Review> reviews) {
        return reviews.stream()
                .map(this::toReviewDto)
                .toList();
    }
}