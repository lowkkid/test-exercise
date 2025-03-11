package com.andersenlab.test.andersentest.integration.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.andersenlab.test.andersentest.dto.ReviewDto;
import com.andersenlab.test.andersentest.factory.ReviewDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

public class ReviewControllerIntegrationTest extends BaseControllerIntegrationTest {

    private static final String REVIEW_CONTROLLER_BASE_URL = "/api/books/{bookId}/reviews";

    @Test
    void getAllReviewsForBook_shouldReturnList_whenReviewsExist() throws Exception {
        Long bookId = 1L;
        mockMvc.perform(get(REVIEW_CONTROLLER_BASE_URL, bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    void createReview_shouldReturnCreatedReview_whenValidInput() throws Exception {
        Long bookId = 1L;
        ReviewDto newReview = ReviewDataFactory.buildReviewDto();
        newReview.setComment("This is a new review from integration test");

        String responseContent = mockMvc.perform(post(REVIEW_CONTROLLER_BASE_URL, bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newReview)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.comment").value("This is a new review from integration test"))
                .andReturn().getResponse().getContentAsString();

        ReviewDto createdReview = objectMapper.readValue(responseContent, ReviewDto.class);
        assertThat(createdReview.getId()).isNotNull();
    }

    @Test
    void updateReview_shouldReturnUpdatedReview_whenReviewExists() throws Exception {
        Long bookId = 1L;
        Long reviewId = 1L;
        ReviewDto updateDto = ReviewDataFactory.buildReviewDto();
        updateDto.setComment("Updated review comment");

        mockMvc.perform(put(REVIEW_CONTROLLER_BASE_URL + "/{reviewId}", bookId, reviewId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(reviewId))
                .andExpect(jsonPath("$.comment").value("Updated review comment"));
    }

    @Test
    void deleteReview_shouldReturnNoContent_whenReviewDeleted() throws Exception {
        Long bookId = 1L;
        ReviewDto newReview = ReviewDataFactory.buildReviewDto();
        newReview.setComment("Review to be deleted");

        String createResponse = mockMvc.perform(post(REVIEW_CONTROLLER_BASE_URL, bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newReview)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        ReviewDto createdReview = objectMapper.readValue(createResponse, ReviewDto.class);
        Long reviewId = createdReview.getId();

        mockMvc.perform(delete(REVIEW_CONTROLLER_BASE_URL + "/{reviewId}", bookId, reviewId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(REVIEW_CONTROLLER_BASE_URL, bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id==" + reviewId + ")]").doesNotExist());
    }
}
