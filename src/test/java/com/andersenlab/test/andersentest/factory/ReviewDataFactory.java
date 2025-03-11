package com.andersenlab.test.andersentest.factory;

import com.andersenlab.test.andersentest.dto.ReviewDto;
import com.andersenlab.test.andersentest.model.Review;

public class ReviewDataFactory {

    private static final Integer RATING = 4;
    private static final String COMMENT = "Comment";

    public static Review buildReview() {
        return new Review(null, RATING, COMMENT, null);
    }

    public static Review buildReview(Long id) {
        return new Review(id, RATING, COMMENT, null);
    }

    public static ReviewDto buildReviewDto() {
        return new ReviewDto(null, RATING, COMMENT);
    }

    public static ReviewDto buildReviewDto(Long id) {
        return new ReviewDto(id, RATING, COMMENT);
    }
}
