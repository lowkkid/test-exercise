package com.andersenlab.test.andersentest.service.impl;

import com.andersenlab.test.andersentest.client.AuthorClient;
import com.andersenlab.test.andersentest.dto.AuthorDetails;
import com.andersenlab.test.andersentest.dto.BookDto;
import com.andersenlab.test.andersentest.dto.BookWithAvgRatingDto;
import com.andersenlab.test.andersentest.dto.BookWithReviewsDto;
import com.andersenlab.test.andersentest.enums.BookSearchField;
import com.andersenlab.test.andersentest.exception.EntityNotFoundException;
import com.andersenlab.test.andersentest.mapper.BookMapper;
import com.andersenlab.test.andersentest.model.Book;
import com.andersenlab.test.andersentest.repository.BookRepository;
import com.andersenlab.test.andersentest.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.andersenlab.test.andersentest.constants.ErrorMessages.BOOK_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final AuthorClient authorClient;


    private static final Integer RATING_THRESHOLD = 4;

    @Override
    public BookDto create(BookDto bookDto) {
        Book book = bookMapper.toBook(bookDto);
        Book savedBook = bookRepository.save(book);
        return bookMapper.toBookDto(savedBook);
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toBookDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BookWithReviewsDto getByIdWithReviews(Long id) {
        Book book = bookRepository.findByIdWithReviews(id)
                .orElseThrow(() -> new EntityNotFoundException(BOOK_NOT_FOUND));
        AuthorDetails authorDetails = authorClient.getAuthorDetails(book.getAuthor());
        return bookMapper.toBookWithReviewsDto(book, authorDetails);
    }

    @Override
    public Book getById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(BOOK_NOT_FOUND));
    }

    @Override
    @Transactional
    public BookDto update(Long id, BookDto bookUpdate) {
        Book existingBook = getById(id);

        existingBook.setTitle(bookUpdate.getTitle());
        existingBook.setAuthor(bookUpdate.getAuthor());
        existingBook.setPublicationYear(bookUpdate.getPublicationYear());
        existingBook.setAvailableCopies(bookUpdate.getAvailableCopies());

        Book savedBook = bookRepository.save(existingBook);
        return bookMapper.toBookDto(savedBook);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Book book = getById(id);
        bookRepository.delete(book);
    }

    @Override
    public List<BookDto> search(String query, BookSearchField bookSearchField) {
        List<Book> result = switch (bookSearchField) {
            case TITLE -> bookRepository.searchByTitle(query);
            case AUTHOR -> bookRepository.searchByAuthor(query);
        };

        return result.stream()
                .map(bookMapper::toBookDto)
                .toList();
    }

    @Override
    public List<BookDto> findAllWithReleaseYearAfter(Integer year) {
        return bookRepository.findAllWithReleaseYearAfter(year).stream()
                .map(bookMapper::toBookDto)
                .toList();
    }

    @Override
    public List<BookWithAvgRatingDto> findAverageRatings() {
        return bookRepository.findAverageRatings();
    }

    @Override
    public List<BookDto> getHighlyRatedBooks(boolean useNative) {
        List<Book> books = useNative
                ? bookRepository.findHighlyRatedBooksNative(RATING_THRESHOLD)
                : bookRepository.findHighlyRatedBooksJpql(RATING_THRESHOLD);

        compareResults();

        return books.stream()
                .map(bookMapper::toBookDto)
                .toList();
    }

    private void compareResults() {
        List<Book> nativeResults = bookRepository.findHighlyRatedBooksNative(RATING_THRESHOLD);
        List<Book> jpqlResults = bookRepository.findHighlyRatedBooksJpql(RATING_THRESHOLD);

        if( nativeResults.containsAll(jpqlResults)
                && jpqlResults.containsAll(nativeResults)) {
            log.info("Both lists are equal");
        } else {
            log.info("Both lists are not equal");
        }
    }
}
