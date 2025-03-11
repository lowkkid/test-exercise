package com.andersenlab.test.andersentest.mapper;

import com.andersenlab.test.andersentest.dto.AuthorDetails;
import com.andersenlab.test.andersentest.dto.BookDto;
import com.andersenlab.test.andersentest.dto.BookWithReviewsDto;
import com.andersenlab.test.andersentest.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ReviewMapper.class)
public interface BookMapper {

    @Mapping(target = "availableCopies", source = "availableCopies", defaultValue = "0")
    Book toBook(BookDto bookDto);

    BookDto toBookDto(Book book);

    @Mapping(target = "book", source = "book")
    @Mapping(target = "reviews", source = "book.reviews")
    @Mapping(target = "authorDetails", source = "authorDetails")
    BookWithReviewsDto toBookWithReviewsDto(Book book, AuthorDetails authorDetails);
}
