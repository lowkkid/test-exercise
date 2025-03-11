package com.andersenlab.test.andersentest.controller.impl;

import com.andersenlab.test.andersentest.controller.BookController;
import com.andersenlab.test.andersentest.dto.BookDto;
import com.andersenlab.test.andersentest.dto.BookWithAvgRatingDto;
import com.andersenlab.test.andersentest.dto.BookWithReviewsDto;
import com.andersenlab.test.andersentest.enums.BookSearchField;
import com.andersenlab.test.andersentest.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookControllerImpl implements BookController {

    private final BookService bookService;

    @Override
    public ResponseEntity<BookDto> create(BookDto book) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.create(book));
    }

    @Override
    public ResponseEntity<List<BookDto>> getAll() {
        return ResponseEntity.ok(bookService.findAll());
    }

    @Override
    public ResponseEntity<BookWithReviewsDto> getById(Long id) {
        return ResponseEntity.ok(bookService.getByIdWithReviews(id));
    }

    @Override
    public ResponseEntity<BookDto> update(Long id, BookDto bookUpdate) {
        return ResponseEntity.ok(bookService.update(id, bookUpdate));
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<BookDto>> search(String query, BookSearchField bookSearchField) {
        return ResponseEntity.ok(bookService.search(query, bookSearchField));
    }

    @Override
    public ResponseEntity<List<BookDto>> getAllWithReleaseYearAfter(Integer year) {
        return ResponseEntity.ok(bookService.findAllWithReleaseYearAfter(year));
    }

    @Override
    public ResponseEntity<List<BookWithAvgRatingDto>> getAverageRatings() {
        return ResponseEntity.ok(bookService.findAverageRatings());
    }

    @Override
    public ResponseEntity<List<BookDto>> getHighlyRatedNative(Boolean useNative) {
        return ResponseEntity.ok(bookService.getHighlyRatedBooks(useNative));
    }
}