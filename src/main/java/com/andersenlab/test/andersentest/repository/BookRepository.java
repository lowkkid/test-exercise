package com.andersenlab.test.andersentest.repository;

import com.andersenlab.test.andersentest.dto.BookWithAvgRatingDto;
import com.andersenlab.test.andersentest.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.reviews WHERE b.id = :id")
    Optional<Book> findByIdWithReviews(Long id);

    @Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Book> searchByTitle(String query);

    @Query("SELECT b FROM Book b WHERE LOWER(b.author) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Book> searchByAuthor(String query);

    @Query(value = "SELECT * FROM book WHERE publication_year >= :year", nativeQuery = true)
    List<Book> findAllWithReleaseYearAfter(Integer year);

    @Query("""
        SELECT new com.andersenlab.test.andersentest.dto.BookWithAvgRatingDto(
            b.title,
            AVG(r.rating)
        )
        FROM Book b
        LEFT JOIN b.reviews r
        GROUP BY b.id, b.title
    """)
    List<BookWithAvgRatingDto> findAverageRatings();

    @Query(value = """
        SELECT b.*
        FROM book b
        JOIN review r ON b.id = r.book_id
        GROUP BY b.id
        HAVING AVG(r.rating) >= :threshold
    """, nativeQuery = true)
    List<Book> findHighlyRatedBooksNative(Integer threshold);

    @Query("""
        SELECT b
        FROM Book b
        JOIN b.reviews r
        GROUP BY b
        HAVING AVG(r.rating) >= :threshold
    """)
    List<Book> findHighlyRatedBooksJpql(Integer threshold);
}
