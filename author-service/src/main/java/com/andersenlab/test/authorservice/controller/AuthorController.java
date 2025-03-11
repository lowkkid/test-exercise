package com.andersenlab.test.authorservice.controller;

import com.andersenlab.test.authorservice.dto.AuthorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    Map<String, AuthorDetails> authors = new HashMap<>();

    {
        authors.put("F. Scott Fitzgerald", new AuthorDetails("F. Scott Fitzgerald", "A renowned Java developer with 10+ years of experience",
                "American"));
        authors.put("George Orwell", new AuthorDetails("George Orwell", "A renowned Python developer with 10+ years of experience",
                "Canadian"));
        authors.put("Harper Lee", new AuthorDetails("Harper Lee", "A renowned JS developer with 10+ years of experience",
                "Belarusian"));
        authors.put("string", new AuthorDetails("Harper Lee", "A renowned JS developer with 10+ years of experience",
                "mock"));
    }

    @GetMapping("/{authorName}")
    public ResponseEntity<AuthorDetails> getAuthorDetails(@PathVariable String authorName) {
        AuthorDetails authorDetails = authors.get(authorName);
        if (authorDetails == null) {
            return ResponseEntity.notFound().build();
        }
        if (Math.random() > 0.7) {
            return ResponseEntity.ok(authorDetails);
        } else {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }
}