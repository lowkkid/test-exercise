package com.andersenlab.test.andersentest.model;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Entity
@Table(name = "book")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    @NotBlank(message = "Title cannot be blank")
    private String title;

    @Column(name = "author", nullable = false)
    @NotBlank(message = "Author cannot be blank")
    private String author;

    @Column(name = "publication_year", nullable = false)
    @NotNull
    private Integer publicationYear;

    @Column(name = "available_copies", nullable = false)
    @NotNull
    @Min(value = 0, message = "Available copies cannot be negative")
    private Integer availableCopies = 0;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    //to make hibernate delete related reviews in one query, not with N=reviews.size queries
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Review> reviews;
}