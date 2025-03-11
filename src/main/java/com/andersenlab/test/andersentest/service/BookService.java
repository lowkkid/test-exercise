package com.andersenlab.test.andersentest.service;

import com.andersenlab.test.andersentest.dto.BookDto;
import com.andersenlab.test.andersentest.dto.BookWithAvgRatingDto;
import com.andersenlab.test.andersentest.dto.BookWithReviewsDto;
import com.andersenlab.test.andersentest.enums.BookSearchField;
import com.andersenlab.test.andersentest.model.Book;

import java.util.List;

public interface BookService {

    BookDto create(BookDto bookDto);

    List<BookDto> findAll();

    BookWithReviewsDto getByIdWithReviews(Long id);

    BookDto update(Long id, BookDto bookUpdate);

    void delete(Long id);

    List<BookDto> search(String query, BookSearchField bookSearchField);

    List<BookDto> findAllWithReleaseYearAfter(Integer year);

    List<BookWithAvgRatingDto> findAverageRatings();

    List<BookDto> getHighlyRatedBooks(boolean useNative);

    Book getById(Long id);
}
