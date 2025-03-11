package com.andersenlab.test.andersentest.config;

import com.andersenlab.test.andersentest.utils.BookSearchFieldConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new BookSearchFieldConverter());
    }
}
