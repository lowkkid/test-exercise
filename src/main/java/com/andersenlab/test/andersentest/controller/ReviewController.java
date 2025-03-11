package com.andersenlab.test.andersentest.controller;

import com.andersenlab.test.andersentest.dto.ReviewDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/api/books/{bookId}/reviews")
public interface ReviewController {

    @Operation(
            summary = "Create a Review",
            description = "Creates a new review for the specified book."
    )
    @PostMapping
    ResponseEntity<ReviewDto> createReview(@PathVariable Long bookId, @RequestBody @Valid ReviewDto reviewDto);

    @Operation(
            summary = "Get All Reviews for a Book",
            description = "Retrieves a list of all reviews for the specified book."
    )
    @GetMapping
    ResponseEntity<List<ReviewDto>> getAllReviewsForBook(@PathVariable Long bookId);

    @Operation(
            summary = "Update a Review",
            description = "Updates an existing review for the specified book and review ID."
    )
    @PutMapping("/{reviewId}")
    ResponseEntity<ReviewDto> updateReview(@PathVariable Long bookId, @PathVariable Long reviewId, @RequestBody @Valid ReviewDto reviewDto);

    @Operation(
            summary = "Delete a Review",
            description = "Deletes the review with the specified review ID for the given book."
    )
    @DeleteMapping("/{reviewId}")
    ResponseEntity<Void> deleteReview(@PathVariable Long bookId, @PathVariable Long reviewId);
}
