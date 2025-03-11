package com.andersenlab.test.andersentest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookWithReviewsDto {

    //here i used composition to avoid code duplicating, because otherwise i would duplicate all fields from BookDto
    BookDto book;

    List<ReviewDto> reviews;

    AuthorDetails authorDetails;
}
