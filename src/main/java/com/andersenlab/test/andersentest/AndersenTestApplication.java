package com.andersenlab.test.andersentest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AndersenTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(AndersenTestApplication.class, args);
    }

}
