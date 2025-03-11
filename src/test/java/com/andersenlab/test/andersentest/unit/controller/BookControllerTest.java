package com.andersenlab.test.andersentest.unit.controller;

import static com.andersenlab.test.andersentest.factory.BookDataFactory.buildBookDto;
import static com.andersenlab.test.andersentest.factory.BookDataFactory.buildBookWithReviewsDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.andersenlab.test.andersentest.controller.BookController;
import com.andersenlab.test.andersentest.dto.BookDto;
import com.andersenlab.test.andersentest.dto.BookWithReviewsDto;
import com.andersenlab.test.andersentest.service.BookService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(BookController.class)
@AutoConfigureMockMvc
@WebAppConfiguration
class BookControllerTest {

    private static final String BOOK_ENDPOINT = "/api/books";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() {
        mockMvc = org.springframework.test.web.servlet.setup.MockMvcBuilders
                .webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void createBook_shouldReturnCreated_whenSuccessfullyCreated() throws Exception {
        BookDto bookDto = buildBookDto();

        BookDto createdBook = buildBookDto(1L);
        when(bookService.create(bookDto)).thenReturn(createdBook);

        MvcResult result = mockMvc.perform(post(BOOK_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        String expectedResponse = objectMapper.writeValueAsString(createdBook);
        assertEquals(expectedResponse, result.getResponse().getContentAsString());
    }

    @Test
    void getAllBooks_shouldReturnList_whenBooksExist() throws Exception {
        List<BookDto> books = Arrays.asList(
                buildBookDto(),
                buildBookDto()
        );
        when(bookService.findAll()).thenReturn(books);

        MvcResult result = mockMvc.perform(get(BOOK_ENDPOINT))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        List<BookDto> returnedBooks = objectMapper.readValue(responseBody,
                new TypeReference<>() {});
        assertEquals(books.size(), returnedBooks.size());
    }

    @Test
    void getBookById_shouldReturnBookWithReviews_whenBookExists() throws Exception {
        Long bookId = 1L;
        BookWithReviewsDto bookWithReviewsDto = buildBookWithReviewsDto();

        when(bookService.getByIdWithReviews(bookId)).thenReturn(bookWithReviewsDto);

        MvcResult result = mockMvc.perform(get(BOOK_ENDPOINT + "/" + bookId))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String expectedResponse = objectMapper.writeValueAsString(bookWithReviewsDto);
        assertEquals(expectedResponse, result.getResponse().getContentAsString());
    }

    @Test
    void deleteBook_shouldReturnNoContent_whenSuccessfullyDeleted() throws Exception {
        Long bookId = 1L;
        doNothing().when(bookService).delete(bookId);

        mockMvc.perform(delete(BOOK_ENDPOINT + "/" + bookId))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void updateBook_shouldReturnUpdated_whenSuccessfullyUpdated() throws Exception {
        Long bookId = 1L;
        BookDto updateDto = buildBookDto();
        BookDto updatedBook = buildBookDto(bookId);
        when(bookService.update(bookId, updateDto)).thenReturn(updatedBook);

        MvcResult result = mockMvc.perform(put(BOOK_ENDPOINT + "/" + bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String expectedResponse = objectMapper.writeValueAsString(updatedBook);
        assertEquals(expectedResponse, result.getResponse().getContentAsString());
    }
}
