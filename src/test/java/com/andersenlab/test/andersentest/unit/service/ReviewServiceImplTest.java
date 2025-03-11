package com.andersenlab.test.andersentest.unit.service;


import static com.andersenlab.test.andersentest.constants.ErrorMessages.REVIEW_DOES_NOT_BELONG_TO_BOOK;
import static com.andersenlab.test.andersentest.constants.ErrorMessages.REVIEW_NOT_FOUND;
import static com.andersenlab.test.andersentest.factory.BookDataFactory.buildBook;
import static com.andersenlab.test.andersentest.factory.ReviewDataFactory.buildReview;
import static com.andersenlab.test.andersentest.factory.ReviewDataFactory.buildReviewDto;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.andersenlab.test.andersentest.dto.ReviewDto;
import com.andersenlab.test.andersentest.exception.AccessRestrictedException;
import com.andersenlab.test.andersentest.exception.EntityNotFoundException;
import com.andersenlab.test.andersentest.mapper.ReviewMapper;
import com.andersenlab.test.andersentest.model.Book;
import com.andersenlab.test.andersentest.model.Review;
import com.andersenlab.test.andersentest.repository.ReviewRepository;
import com.andersenlab.test.andersentest.service.BookService;
import com.andersenlab.test.andersentest.service.impl.ReviewServiceImpl;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceImplTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ReviewMapper reviewMapper;

    @Mock
    private BookService bookService;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @Test
    public void create_shouldReturnReviewDto_whenValidInput() {
        Long bookId = 1L;
        ReviewDto inputReviewDto = buildReviewDto();
        Review review = buildReview();
        Book book = buildBook(bookId);
        Review savedReview = buildReview(1L);
        savedReview.setBook(book);
        ReviewDto savedReviewDto = buildReviewDto(1L);

        when(bookService.getById(bookId)).thenReturn(book);
        when(reviewMapper.toReview(inputReviewDto)).thenReturn(review);
        when(reviewRepository.save(any(Review.class))).thenReturn(savedReview);
        when(reviewMapper.toReviewDto(savedReview)).thenReturn(savedReviewDto);

        ReviewDto result = reviewService.create(bookId, inputReviewDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(bookService).getById(bookId);
        verify(reviewMapper).toReview(inputReviewDto);
        verify(reviewMapper).toReviewDto(savedReview);
    }

    @Test
    public void findAllForBook_shouldReturnListOfReviewDto_whenBookExists() {
        Long bookId = 1L;
        Book book = buildBook(bookId);
        Review review = buildReview(1L);
        review.setBook(book);
        ReviewDto reviewDto = buildReviewDto(1L);

        when(bookService.getById(bookId)).thenReturn(book);
        when(reviewRepository.findByBookId(bookId)).thenReturn(Collections.singletonList(review));
        when(reviewMapper.toReviewDto(review)).thenReturn(reviewDto);

        var result = reviewService.findAllForBook(bookId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(bookService).getById(bookId);
        verify(reviewRepository).findByBookId(bookId);
    }

    @Test
    public void get_shouldReturnReview_whenReviewExists() {
        Long reviewId = 1L;
        Review review = buildReview(reviewId);
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

        Review result = reviewService.get(reviewId);

        assertNotNull(result);
        assertEquals(reviewId, result.getId());
        verify(reviewRepository).findById(reviewId);
    }

    @Test
    public void get_shouldThrowEntityNotFoundException_whenReviewNotFound() {
        Long reviewId = 1L;
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> reviewService.get(reviewId));
        assertEquals(String.format(REVIEW_NOT_FOUND, reviewId), exception.getMessage());
        verify(reviewRepository).findById(reviewId);
    }

    @Test
    public void update_shouldReturnUpdatedReviewDto_whenReviewBelongsToBook() {
        Long bookId = 1L;
        Long reviewId = 1L;
        String updatedComment = "Updated Comment";
        Review existingReview = buildReview(reviewId);
        Book book = buildBook(bookId);
        existingReview.setBook(book);

        ReviewDto updateDto = buildReviewDto();
        updateDto.setComment(updatedComment);

        Review updatedReview = buildReview(reviewId);
        updatedReview.setBook(book);
        updatedReview.setComment(updatedComment);
        ReviewDto updatedReviewDto = buildReviewDto(reviewId);
        updatedReviewDto.setComment(updatedComment);

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(existingReview));
        when(reviewRepository.save(existingReview)).thenReturn(updatedReview);
        when(reviewMapper.toReviewDto(updatedReview)).thenReturn(updatedReviewDto);

        ReviewDto result = reviewService.update(bookId, reviewId, updateDto);

        assertNotNull(result);
        assertEquals(updatedComment, result.getComment());
        verify(reviewRepository).findById(reviewId);
        verify(reviewRepository).save(existingReview);
        verify(reviewMapper).toReviewDto(updatedReview);
    }

    @Test
    public void update_shouldThrowAccessRestrictedException_whenReviewDoesNotBelongToBook() {
        Long bookId = 1L;
        Long reviewId = 1L;
        Review existingReview = buildReview(reviewId);
        Book book = buildBook(2L);
        existingReview.setBook(book);
        String updatedComment = "Updated Comment";

        ReviewDto updateDto = buildReviewDto();
        updateDto.setComment(updatedComment);

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(existingReview));

        AccessRestrictedException exception = assertThrows(AccessRestrictedException.class, () -> reviewService.update(bookId, reviewId, updateDto));
        assertEquals(String.format(REVIEW_DOES_NOT_BELONG_TO_BOOK, reviewId, bookId), exception.getMessage());
        verify(reviewRepository).findById(reviewId);
    }

    @Test
    public void delete_shouldCallRepositoryDelete_whenReviewBelongsToBook() {
        Long bookId = 1L;
        Long reviewId = 1L;
        Review existingReview = buildReview(reviewId);
        Book book = buildBook(bookId);
        existingReview.setBook(book);

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(existingReview));
        doNothing().when(reviewRepository).delete(existingReview);

        reviewService.delete(bookId, reviewId);

        verify(reviewRepository).findById(reviewId);
        verify(reviewRepository).delete(existingReview);
    }

    @Test
    public void delete_shouldThrowAccessRestrictedException_whenReviewDoesNotBelongToBook() {
        Long bookId = 1L;
        Long reviewId = 1L;
        Review existingReview = buildReview(reviewId);
        Book book = buildBook(2L);
        existingReview.setBook(book);

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(existingReview));

        AccessRestrictedException exception = assertThrows(AccessRestrictedException.class, () -> reviewService.delete(bookId, reviewId));
        assertEquals(String.format(REVIEW_DOES_NOT_BELONG_TO_BOOK, reviewId, bookId), exception.getMessage());
        verify(reviewRepository).findById(reviewId);
    }
}
