package com.andersenlab.test.andersentest.controller.impl;

import com.andersenlab.test.andersentest.controller.ReviewController;
import com.andersenlab.test.andersentest.dto.ReviewDto;
import com.andersenlab.test.andersentest.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewControllerImpl implements ReviewController {

    private final ReviewService reviewService;

    @Override
    public ResponseEntity<ReviewDto> createReview(Long bookId, ReviewDto reviewDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reviewService.create(bookId, reviewDto));
    }

    @Override
    public ResponseEntity<List<ReviewDto>> getAllReviewsForBook(Long bookId) {
        return ResponseEntity.ok(reviewService.findAllForBook(bookId));
    }

    @Override
    public ResponseEntity<ReviewDto> updateReview(Long bookId, Long reviewId, ReviewDto reviewDto) {
        return ResponseEntity.ok(reviewService.update(bookId, reviewId, reviewDto));
    }

    @Override
    public ResponseEntity<Void> deleteReview(Long bookId, Long reviewId) {
        reviewService.delete(bookId, reviewId);
        return ResponseEntity.noContent().build();
    }
}