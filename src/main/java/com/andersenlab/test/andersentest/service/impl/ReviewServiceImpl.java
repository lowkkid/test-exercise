package com.andersenlab.test.andersentest.service.impl;

import com.andersenlab.test.andersentest.dto.ReviewDto;
import com.andersenlab.test.andersentest.exception.AccessRestrictedException;
import com.andersenlab.test.andersentest.exception.EntityNotFoundException;
import com.andersenlab.test.andersentest.mapper.ReviewMapper;
import com.andersenlab.test.andersentest.model.Book;
import com.andersenlab.test.andersentest.model.Review;
import com.andersenlab.test.andersentest.repository.ReviewRepository;
import com.andersenlab.test.andersentest.service.BookService;
import com.andersenlab.test.andersentest.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.andersenlab.test.andersentest.constants.ErrorMessages.REVIEW_DOES_NOT_BELONG_TO_BOOK;
import static com.andersenlab.test.andersentest.constants.ErrorMessages.REVIEW_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final BookService bookService;

    @Override
    @Transactional
    public ReviewDto create(Long bookId, ReviewDto reviewDto) {
        Book book = bookService.getById(bookId);
        Review review = reviewMapper.toReview(reviewDto);
        review.setBook(book);

        Review savedReview = reviewRepository.save(review);
        return reviewMapper.toReviewDto(savedReview);
    }

    @Override
    public List<ReviewDto> findAllForBook(Long bookId) {
        //just to check, if book exists in system. if not - exception will be thrown
        bookService.getById(bookId);

        return reviewRepository.findByBookId(bookId).stream()
                .map(reviewMapper::toReviewDto)
                .toList();
    }

    @Override
    public Review get(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(REVIEW_NOT_FOUND, id)));
    }

    @Override
    @Transactional
    public ReviewDto update(Long bookId, Long reviewId, ReviewDto reviewDto) {
        Review review = get(reviewId);
        validateBelongingToBook(review, bookId);

        review.setRating(review.getRating());
        review.setComment(reviewDto.getComment());

        Review updatedReview = reviewRepository.save(review);
        return reviewMapper.toReviewDto(updatedReview);
    }

    @Override
    public void delete(Long bookId, Long reviewId) {
        Review review = get(reviewId);
        validateBelongingToBook(review, bookId);
        reviewRepository.delete(review);
    }

    private void validateBelongingToBook(Review review, Long bookId) {
        if (!review.getBook().getId().equals(bookId)) {
            throw new AccessRestrictedException(String.format(REVIEW_DOES_NOT_BELONG_TO_BOOK, review.getId(), bookId));
        }
    }
}
