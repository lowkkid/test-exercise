package com.andersenlab.test.andersentest.unit.controller;

import static com.andersenlab.test.andersentest.factory.ReviewDataFactory.buildReviewDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.andersenlab.test.andersentest.controller.ReviewController;
import com.andersenlab.test.andersentest.dto.ReviewDto;
import com.andersenlab.test.andersentest.service.ReviewService;
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

@WebMvcTest(ReviewController.class)
@AutoConfigureMockMvc
@WebAppConfiguration
class ReviewControllerTest {

    private static final String REVIEW_ENDPOINT = "/api/books/{bookId}/reviews";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReviewService reviewService;

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
    void createReview_shouldReturnCreated_whenSuccessfullyCreated() throws Exception {
        Long bookId = 1L;
        ReviewDto reviewDto = buildReviewDto();

        ReviewDto createdReview = buildReviewDto(1L);

        when(reviewService.create(bookId, reviewDto)).thenReturn(createdReview);

        MvcResult result = mockMvc.perform(post(REVIEW_ENDPOINT, bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        String expectedResponse = objectMapper.writeValueAsString(createdReview);
        assertEquals(expectedResponse, result.getResponse().getContentAsString());
    }

    @Test
    void getAllReviewsForBook_shouldReturnList_whenReviewsExist() throws Exception {
        Long bookId = 1L;
        List<ReviewDto> reviews = Arrays.asList(
                buildReviewDto(1L),
                buildReviewDto(2L)
        );
       when(reviewService.findAllForBook(bookId)).thenReturn(reviews);

        MvcResult result = mockMvc.perform(get(REVIEW_ENDPOINT, bookId))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        List<ReviewDto> returnedReviews = objectMapper.readValue(responseBody,
                new TypeReference<>() {});
        assertEquals(reviews.size(), returnedReviews.size());
    }

    @Test
    void updateReview_shouldReturnUpdated_whenSuccessfullyUpdated() throws Exception {
        Long bookId = 1L;
        Long reviewId = 1L;
        ReviewDto updateDto = buildReviewDto();
        ReviewDto updatedReview = buildReviewDto(reviewId);

        when(reviewService.update(bookId, reviewId, updateDto)).thenReturn(updatedReview);

        MvcResult result = mockMvc.perform(put(REVIEW_ENDPOINT + "/{reviewId}", bookId, reviewId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String expectedResponse = objectMapper.writeValueAsString(updatedReview);
        assertEquals(expectedResponse, result.getResponse().getContentAsString());
    }

    @Test
    void deleteReview_shouldReturnNoContent_whenSuccessfullyDeleted() throws Exception {
        Long bookId = 1L;
        Long reviewId = 1L;
        doNothing().when(reviewService).delete(bookId, reviewId);

        mockMvc.perform(delete(REVIEW_ENDPOINT + "/{reviewId}", bookId, reviewId))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}
