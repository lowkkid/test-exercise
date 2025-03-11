package com.andersenlab.test.andersentest.integration.controller;

import com.andersenlab.test.andersentest.client.AuthorClient;
import com.andersenlab.test.andersentest.dto.AuthorDetails;
import org.junit.jupiter.api.Test;


import static com.andersenlab.test.andersentest.factory.BookDataFactory.buildBookDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.andersenlab.test.andersentest.dto.BookDto;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

public class BookControllerIntegrationTest extends BaseControllerIntegrationTest {

    private static final String BOOK_CONTROLLER_BASE_URL = "/api/books";

    @MockitoBean
    private AuthorClient authorClient;

    @Test
    void connectionEstablished() {
        assertThat(postgreSQLContainer.isCreated()).isTrue();
        assertThat(postgreSQLContainer.isRunning()).isTrue();
    }

    @Test
    void getAllBooks_shouldReturnList_whenBooksExist() throws Exception {
        mockMvc.perform(get(BOOK_CONTROLLER_BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(greaterThanOrEqualTo(21)));
    }

    @Test
    void getBookById_shouldReturnBook_whenBookExists() throws Exception {
        when(authorClient.getAuthorDetails(anyString()))
                .thenReturn(new AuthorDetails("dummy", "dummy biography", "dummy nationality"));

        mockMvc.perform(get(BOOK_CONTROLLER_BASE_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.book.id").value(1))
                .andExpect(jsonPath("$.book.title").value("The Great Gatsby"))
                .andExpect(jsonPath("$.book.author").value("F. Scott Fitzgerald"));
    }

    @Test
    void getBookById_shouldReturn404_whenBookNotExists() throws Exception {
        mockMvc.perform(get(BOOK_CONTROLLER_BASE_URL + "/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createBook_shouldReturn201_whenBookCreated() throws Exception {
        BookDto newBook = buildBookDto();

        String responseContent = mockMvc.perform(post(BOOK_CONTROLLER_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newBook)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Title"))
                .andExpect(jsonPath("$.author").value("Author"))
                .andReturn().getResponse().getContentAsString();

        BookDto createdBook = objectMapper.readValue(responseContent, BookDto.class);
        assertThat(createdBook.getId()).isNotNull();
    }

    @Test
    void updateBook_shouldReturnUpdatedBook_whenBookExists() throws Exception {
        BookDto bookToCreate = buildBookDto();
        bookToCreate.setTitle("Original Title");
        bookToCreate.setAuthor("Original Author");

        String createResponse = mockMvc.perform(post(BOOK_CONTROLLER_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookToCreate)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        BookDto createdBook = objectMapper.readValue(createResponse, BookDto.class);
        Long bookId = createdBook.getId();

        BookDto updateDto = buildBookDto();
        updateDto.setTitle("Updated Title");
        updateDto.setAuthor("Updated Author");
        updateDto.setPublicationYear(createdBook.getPublicationYear());
        updateDto.setAvailableCopies(createdBook.getAvailableCopies());

        mockMvc.perform(put(BOOK_CONTROLLER_BASE_URL + "/" + bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookId))
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.author").value("Updated Author"));
    }

    @Test
    void deleteBook_shouldReturn204_whenBookDeleted() throws Exception {
        BookDto bookToCreate = buildBookDto();
        bookToCreate.setTitle("To be deleted");
        bookToCreate.setAuthor("Delete Author");

        String createResponse = mockMvc.perform(post(BOOK_CONTROLLER_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookToCreate)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        BookDto createdBook = objectMapper.readValue(createResponse, BookDto.class);
        Long bookId = createdBook.getId();

        mockMvc.perform(delete(BOOK_CONTROLLER_BASE_URL + "/" + bookId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(BOOK_CONTROLLER_BASE_URL + "/" + bookId))
                .andExpect(status().isNotFound());
    }
}
