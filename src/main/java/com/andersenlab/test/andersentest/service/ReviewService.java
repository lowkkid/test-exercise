package com.andersenlab.test.andersentest.service;

import com.andersenlab.test.andersentest.dto.ReviewDto;
import com.andersenlab.test.andersentest.model.Review;

import java.util.List;

public interface ReviewService {

    ReviewDto create(Long bookId, ReviewDto reviewDto);

    List<ReviewDto> findAllForBook(Long bookId);

    Review get(Long id);

    ReviewDto update(Long reviewId, Long bookId, ReviewDto reviewDto);

    void delete(Long bookId, Long reviewId);
}
