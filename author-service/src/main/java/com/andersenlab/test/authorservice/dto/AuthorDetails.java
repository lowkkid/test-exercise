package com.andersenlab.test.authorservice.dto;

public record AuthorDetails(
        String name,
        String biography,
        String nationality
) {}