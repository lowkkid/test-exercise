package com.andersenlab.test.andersentest.integration.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.andersenlab.test.andersentest.dto.BookWithAvgRatingDto;
import com.andersenlab.test.andersentest.model.Book;
import com.andersenlab.test.andersentest.repository.BookRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BookRepositoryIntegrationTest extends BaseRepositoryIntegrationTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    void findByIdWithReviews_shouldReturnBookWithReviews_whenBookExists() {
        Optional<Book> optionalBook = bookRepository.findByIdWithReviews(1L);
        assertThat(optionalBook).isPresent();
        Book book = optionalBook.get();
        assertThat(book.getReviews()).hasSize(3);
    }

    @Test
    void searchByTitle_shouldReturnBooks_whenQueryMatchesTitle() {
        List<Book> books = bookRepository.searchByTitle("gatsby");
        assertThat(books).isNotEmpty();
        boolean found = books.stream()
                .anyMatch(b -> "The Great Gatsby".equalsIgnoreCase(b.getTitle()));
        assertThat(found).isTrue();
    }

    @Test
    void searchByAuthor_shouldReturnBooks_whenQueryMatchesAuthor() {
        List<Book> books = bookRepository.searchByAuthor("tolstoy");
        assertThat(books).hasSize(2);
        books.forEach(book ->
                assertThat(book.getAuthor().toLowerCase()).contains("tolstoy")
        );
    }

    @Test
    void findAllWithReleaseYearAfter_shouldReturnBooks_whenPublicationYearAfterGivenYear() {
        List<Book> books = bookRepository.findAllWithReleaseYearAfter(1900);
        assertThat(books).hasSize(7);
    }

    @Test
    void findAverageRatings_shouldReturnAverageRatings_forAllBooksWithReviews() {
        List<BookWithAvgRatingDto> avgRatings = bookRepository.findAverageRatings();
        assertThat(avgRatings).isNotEmpty();
        BookWithAvgRatingDto gatsbyRating = avgRatings.stream()
                .filter(dto -> "The Great Gatsby".equals(dto.title()))
                .findFirst()
                .orElse(null);
        assertThat(gatsbyRating).isNotNull();
        assertThat(gatsbyRating.averageRating()).isEqualTo(4.0);
    }

    @Test
    void findHighlyRatedBooksNative_shouldReturnBooks_whenAverageRatingAboveThreshold() {
        List<Book> books = bookRepository.findHighlyRatedBooksNative(4);
        assertThat(books).hasSize(17);
    }

    @Test
    void findHighlyRatedBooksJpql_shouldReturnBooks_whenAverageRatingAboveThreshold() {
        List<Book> books = bookRepository.findHighlyRatedBooksJpql(4);
        assertThat(books).hasSize(17);
    }
}
