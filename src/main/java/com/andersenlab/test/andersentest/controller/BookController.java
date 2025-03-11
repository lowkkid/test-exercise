package com.andersenlab.test.andersentest.controller;

import com.andersenlab.test.andersentest.dto.BookDto;
import com.andersenlab.test.andersentest.dto.BookWithAvgRatingDto;
import com.andersenlab.test.andersentest.dto.BookWithReviewsDto;
import com.andersenlab.test.andersentest.enums.BookSearchField;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/api/books")
public interface BookController {

    @Operation(
            summary = "Create a Book",
            description = "Creates a new book based on the provided data."
    )
    @PostMapping
    ResponseEntity<BookDto> create(@RequestBody @Valid BookDto book);

    @Operation(
            summary = "Get All Books",
            description = "Retrieves a list of all books."
    )
    @GetMapping
    ResponseEntity<List<BookDto>> getAll();

    @Operation(
            summary = "Get Book by ID",
            description = "Retrieves a book by its ID, including its reviews and author details (External author service is unavailable with 30% chance) "
    )
    @GetMapping("/{id}")
    ResponseEntity<BookWithReviewsDto> getById(@PathVariable Long id);

    @Operation(
            summary = "Update a Book",
            description = "Updates the book with the specified ID using the provided data."
    )
    @PutMapping("/{id}")
    ResponseEntity<BookDto> update(@PathVariable Long id, @RequestBody @Valid BookDto bookUpdate);

    @Operation(
            summary = "Delete a Book",
            description = "Deletes the book with the specified ID."
    )
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id);

    @Operation(
            summary = "Search Books",
            description = "Searches for books using the specified query and search field (either author or title)."
    )
    @GetMapping("/search")
    ResponseEntity<List<BookDto>> search(@RequestParam String query, @RequestParam BookSearchField bookSearchField);

    @Operation(
            summary = "Get Books Published After a Specific Year",
            description = "Retrieves a list of books that were released after the specified year."
    )
    @GetMapping("/published-after")
    ResponseEntity<List<BookDto>> getAllWithReleaseYearAfter(@RequestParam Integer year);

    @Operation(
            summary = "Get Average Ratings",
            description = "Retrieves a list of book title along with their average ratings."
    )
    @GetMapping("/average-ratings")
    ResponseEntity<List<BookWithAvgRatingDto>> getAverageRatings();

    @Operation(
            summary = "Get Highly Rated Books (Rating > 4)",
            description = "Retrieves a list of highly rated books using a native SQL query or JPA query."
    )
    @GetMapping("/highly-rated")
    ResponseEntity<List<BookDto>> getHighlyRatedNative(@RequestParam Boolean useNative);
}
