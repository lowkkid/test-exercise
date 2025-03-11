package com.andersenlab.test.andersentest.repository;

import com.andersenlab.test.andersentest.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByBookId(Long bookId);

}
