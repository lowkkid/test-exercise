package com.andersenlab.test.andersentest.client;

import com.andersenlab.test.andersentest.config.AuthorFeignConfig;
import com.andersenlab.test.andersentest.dto.AuthorDetails;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "author-service",
        url = "http://localhost:8081/api",
        configuration = AuthorFeignConfig.class

)
public interface AuthorClient {

    @GetMapping("/authors/{authorName}")
    AuthorDetails getAuthorDetails(@PathVariable String authorName);
}