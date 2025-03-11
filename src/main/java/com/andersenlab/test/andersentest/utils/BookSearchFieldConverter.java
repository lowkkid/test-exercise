package com.andersenlab.test.andersentest.utils;

import com.andersenlab.test.andersentest.enums.BookSearchField;
import org.springframework.core.convert.converter.Converter;

public class BookSearchFieldConverter implements Converter<String, BookSearchField> {

    @Override
    public BookSearchField convert(String source) {
        return BookSearchField.getByString(source);
    }
}
