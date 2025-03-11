package com.andersenlab.test.andersentest.unit.service;


import static com.andersenlab.test.andersentest.constants.ErrorMessages.BOOK_NOT_FOUND;
import static com.andersenlab.test.andersentest.factory.BookDataFactory.buildAuthorDetails;
import static com.andersenlab.test.andersentest.factory.BookDataFactory.buildBook;
import static com.andersenlab.test.andersentest.factory.BookDataFactory.buildBookDto;
import static com.andersenlab.test.andersentest.factory.BookDataFactory.buildBookWithAvgRatingDto;
import static com.andersenlab.test.andersentest.factory.BookDataFactory.buildBookWithReviewsDto;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.andersenlab.test.andersentest.client.AuthorClient;
import com.andersenlab.test.andersentest.dto.AuthorDetails;
import com.andersenlab.test.andersentest.dto.BookDto;
import com.andersenlab.test.andersentest.dto.BookWithReviewsDto;
import com.andersenlab.test.andersentest.dto.BookWithAvgRatingDto;
import com.andersenlab.test.andersentest.enums.BookSearchField;
import com.andersenlab.test.andersentest.exception.EntityNotFoundException;
import com.andersenlab.test.andersentest.mapper.BookMapper;
import com.andersenlab.test.andersentest.model.Book;
import com.andersenlab.test.andersentest.repository.BookRepository;
import com.andersenlab.test.andersentest.service.impl.BookServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private AuthorClient authorClient;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    public void create_shouldReturnBookDto_whenValidInput() {
        BookDto bookDto = buildBookDto();
        Book book = buildBook();
        Book savedBook = buildBook(1L);
        BookDto savedBookDto = buildBookDto(1L);

        when(bookMapper.toBook(bookDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(savedBook);
        when(bookMapper.toBookDto(savedBook)).thenReturn(savedBookDto);

        BookDto result = bookService.create(bookDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(bookMapper).toBook(bookDto);
        verify(bookRepository).save(book);
        verify(bookMapper).toBookDto(savedBook);
    }

    @Test
    public void findAll_shouldReturnListOfBookDto_whenBooksFound() {
        Book book1 = buildBook(1L);
        Book book2 = buildBook(2L);
        BookDto bookDto1 = buildBookDto(1L);
        BookDto bookDto2 = buildBookDto(2L);

        when(bookRepository.findAll()).thenReturn(Arrays.asList(book1, book2));
        when(bookMapper.toBookDto(book1)).thenReturn(bookDto1);
        when(bookMapper.toBookDto(book2)).thenReturn(bookDto2);

        List<BookDto> result = bookService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(bookRepository).findAll();
    }

    @Test
    public void getByIdWithReviews_shouldReturnBookWithReviewsDto_whenBookExists() {
        Long bookId = 1L;
        Book book = buildBook(bookId);
        AuthorDetails authorDetails = buildAuthorDetails();
        BookWithReviewsDto bookWithReviewsDto = buildBookWithReviewsDto();

        when(bookRepository.findByIdWithReviews(bookId)).thenReturn(Optional.of(book));
        when(authorClient.getAuthorDetails(book.getAuthor())).thenReturn(authorDetails);
        when(bookMapper.toBookWithReviewsDto(book, authorDetails)).thenReturn(bookWithReviewsDto);

        BookWithReviewsDto result = bookService.getByIdWithReviews(bookId);

        assertNotNull(result);
        verify(bookRepository).findByIdWithReviews(bookId);
        verify(authorClient).getAuthorDetails(book.getAuthor());
        verify(bookMapper).toBookWithReviewsDto(book, authorDetails);
    }

    @Test
    public void getByIdWithReviews_shouldThrowEntityNotFoundException_whenBookNotFound() {
        Long bookId = 1L;
        when(bookRepository.findByIdWithReviews(bookId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> bookService.getByIdWithReviews(bookId));
        assertEquals(BOOK_NOT_FOUND, exception.getMessage());
        verify(bookRepository).findByIdWithReviews(bookId);
    }

    @Test
    public void getById_shouldReturnBook_whenBookExists() {
        Long bookId = 1L;
        Book book = buildBook(bookId);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        Book result = bookService.getById(bookId);
        assertNotNull(result);
        assertEquals(bookId, result.getId());
        verify(bookRepository).findById(bookId);
    }

    @Test
    public void getById_shouldThrowEntityNotFoundException_whenBookNotFound() {
        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> bookService.getById(bookId));
        assertEquals(BOOK_NOT_FOUND, exception.getMessage());
        verify(bookRepository).findById(bookId);
    }

    @Test
    public void update_shouldReturnUpdatedBookDto_whenBookExists() {
        Long bookId = 1L;
        Book existingBook = buildBook(bookId);
        BookDto bookUpdate = new BookDto(null, "New Title", "New Author", 2021, 5);
        Book updatedBook = buildBook(bookId);
        updatedBook.setTitle(bookUpdate.getTitle());
        updatedBook.setAuthor(bookUpdate.getAuthor());
        updatedBook.setPublicationYear(bookUpdate.getPublicationYear());
        updatedBook.setAvailableCopies(bookUpdate.getAvailableCopies());
        BookDto updatedBookDto = new BookDto(bookId, bookUpdate.getTitle(), bookUpdate.getAuthor(),
                bookUpdate.getPublicationYear(), bookUpdate.getAvailableCopies());

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(existingBook)).thenReturn(updatedBook);
        when(bookMapper.toBookDto(updatedBook)).thenReturn(updatedBookDto);

        BookDto result = bookService.update(bookId, bookUpdate);

        assertNotNull(result);
        assertEquals("New Title", result.getTitle());
        verify(bookRepository).findById(bookId);
        verify(bookRepository).save(existingBook);
        verify(bookMapper).toBookDto(updatedBook);
    }

    @Test
    public void update_shouldThrowEntityNotFoundException_whenBookNotFound() {
        Long bookId = 1L;
        BookDto bookUpdate = new BookDto(null, "New Title", "New Author", 2021, 5);
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> bookService.update(bookId, bookUpdate));
        assertEquals("Book not found", exception.getMessage());
        verify(bookRepository).findById(bookId);
    }

    @Test
    public void delete_shouldCallRepositoryDelete_whenBookExists() {
        Long bookId = 1L;
        Book book = buildBook(bookId);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        doNothing().when(bookRepository).delete(book);

        bookService.delete(bookId);

        verify(bookRepository).findById(bookId);
        verify(bookRepository).delete(book);
    }

    @Test
    public void delete_shouldThrowEntityNotFoundException_whenBookNotFound() {
        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> bookService.delete(bookId));
        assertEquals(BOOK_NOT_FOUND, exception.getMessage());
        verify(bookRepository).findById(bookId);
    }

    @Test
    public void search_shouldReturnListOfBookDto_whenSearchingByTitle() {
        String query = "Title";
        BookSearchField field = BookSearchField.TITLE;
        Book book = buildBook(1L);
        BookDto bookDto = buildBookDto(1L);

        when(bookRepository.searchByTitle(query)).thenReturn(Collections.singletonList(book));
        when(bookMapper.toBookDto(book)).thenReturn(bookDto);

        List<BookDto> result = bookService.search(query, field);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(bookRepository).searchByTitle(query);
    }

    @Test
    public void search_shouldReturnListOfBookDto_whenSearchingByAuthor() {
        String query = "Author";
        BookSearchField field = BookSearchField.AUTHOR;
        Book book = buildBook(1L);
        BookDto bookDto = buildBookDto(1L);

        when(bookRepository.searchByAuthor(query)).thenReturn(Collections.singletonList(book));
        when(bookMapper.toBookDto(book)).thenReturn(bookDto);

        List<BookDto> result = bookService.search(query, field);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(bookRepository).searchByAuthor(query);
    }

    @Test
    public void findAllWithReleaseYearAfter_shouldReturnListOfBookDto_whenBooksFound() {
        int year = 1999;
        Book book = buildBook(1L);
        BookDto bookDto = buildBookDto(1L);

        when(bookRepository.findAllWithReleaseYearAfter(year)).thenReturn(Collections.singletonList(book));
        when(bookMapper.toBookDto(book)).thenReturn(bookDto);

        List<BookDto> result = bookService.findAllWithReleaseYearAfter(year);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(bookRepository).findAllWithReleaseYearAfter(year);
    }

    @Test
    public void findAverageRatings_shouldReturnListOfBookWithAvgRatingDto_whenRatingsFound() {
        BookWithAvgRatingDto ratingDto = buildBookWithAvgRatingDto();
        when(bookRepository.findAverageRatings()).thenReturn(Collections.singletonList(ratingDto));

        List<BookWithAvgRatingDto> result = bookService.findAverageRatings();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(bookRepository).findAverageRatings();
    }
}
