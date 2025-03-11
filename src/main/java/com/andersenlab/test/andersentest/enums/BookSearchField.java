package com.andersenlab.test.andersentest.enums;

import com.andersenlab.test.andersentest.exception.InvalidInputException;
import lombok.Getter;

@Getter
public enum BookSearchField {

    TITLE("title"),
    AUTHOR("author");

    private String fieldName;

    BookSearchField(String fieldName) {
        this.fieldName = fieldName;
    }

    public static BookSearchField getByString(String fieldName) {
        for (BookSearchField field : BookSearchField.values()) {
            if (fieldName.equals(field.getFieldName())) {
                return field;
            }
        }
        throw new InvalidInputException("Invalid sort field: " + fieldName);
    }
}
