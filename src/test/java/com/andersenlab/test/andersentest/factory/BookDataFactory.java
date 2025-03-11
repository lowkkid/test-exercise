package com.andersenlab.test.andersentest.factory;

import com.andersenlab.test.andersentest.dto.AuthorDetails;
import com.andersenlab.test.andersentest.dto.BookDto;
import com.andersenlab.test.andersentest.dto.BookWithAvgRatingDto;
import com.andersenlab.test.andersentest.dto.BookWithReviewsDto;
import com.andersenlab.test.andersentest.model.Book;

import java.util.Collections;

public class BookDataFactory {

    private static final String TITLE = "Title";
    private static final String AUTHOR = "Author";
    private static final Integer PUBLICATION_YEAR = 2000;
    private static final Integer AVAILABLE_COPIES = 1;
    private static final String AUTHOR_BIO = "Author Bio";
    private static final String AUTHOR_NATIONALITY = "Author Nationality";
    private static final Double AVG_RATING = 3.5;

    public static Book buildBook() {
        return new Book(null, TITLE, AUTHOR, PUBLICATION_YEAR, AVAILABLE_COPIES, Collections.emptyList());
    }

    public static Book buildBook(Long id) {
        return new Book(id, TITLE, AUTHOR, PUBLICATION_YEAR, AVAILABLE_COPIES, Collections.emptyList());
    }

    public static BookDto buildBookDto() {
        return new BookDto(null, TITLE, AUTHOR, PUBLICATION_YEAR, AVAILABLE_COPIES);
    }

    public static BookDto buildBookDto(Long id) {
        return new BookDto(id, TITLE, AUTHOR, PUBLICATION_YEAR, AVAILABLE_COPIES);
    }

    public static BookWithAvgRatingDto buildBookWithAvgRatingDto() {
        return new BookWithAvgRatingDto(TITLE, AVG_RATING);
    }

    public static BookWithReviewsDto buildBookWithReviewsDto() {
        return new BookWithReviewsDto(buildBookDto(), Collections.emptyList(), buildAuthorDetails());
    }

    public static AuthorDetails buildAuthorDetails() {
        return new AuthorDetails(AUTHOR, AUTHOR_BIO, AUTHOR_NATIONALITY);

    }
}
