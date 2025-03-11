package com.andersenlab.test.andersentest.config;

import com.andersenlab.test.andersentest.client.error.AuthorErrorDecoder;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthorFeignConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new AuthorErrorDecoder();
    }

    @Bean
    public Retryer retryer() {
        return new Retryer.Default(
                1000,    // initial interval
                5000,   // max interval
                2        // max attempts
        );
    }
}