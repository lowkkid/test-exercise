package com.andersenlab.test.andersentest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class BookDto {

    @Schema(hidden = true)
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String author;

    private Integer publicationYear;

    @Min(value = 0, message = "Available copies cannot be negative")
    private Integer availableCopies;
}
