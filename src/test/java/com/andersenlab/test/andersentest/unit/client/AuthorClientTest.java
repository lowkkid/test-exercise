package com.andersenlab.test.andersentest.unit.client;

import com.andersenlab.test.andersentest.client.AuthorClient;
import com.andersenlab.test.andersentest.client.error.AuthorErrorDecoder;
import com.andersenlab.test.andersentest.dto.AuthorDetails;
import com.andersenlab.test.andersentest.exception.AuthorServiceUnavailableException;
import com.andersenlab.test.andersentest.exception.EntityNotFoundException;
import feign.Feign;
import feign.Request;
import feign.Response;
import feign.jackson.JacksonDecoder;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.openfeign.support.SpringMvcContract;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static com.andersenlab.test.andersentest.constants.ErrorMessages.AUTHOR_NOT_FOUND;
import static com.andersenlab.test.andersentest.constants.ErrorMessages.AUTHOR_SERVICE_UNAVAILABLE;
import static org.junit.jupiter.api.Assertions.*;

public class AuthorClientTest {

    private static final String BASE_URL = "http://localhost:8081/api";

    static class StubClient implements feign.Client {
        private final Response response;

        StubClient(Response response) {
            this.response = response;
        }

        @Override
        public Response execute(Request request, Request.Options options) {
            return response;
        }
    }

    private AuthorClient getClient(Response response) {
        return Feign.builder()
                .client(new StubClient(response))
                .decoder(new JacksonDecoder())
                .errorDecoder(new AuthorErrorDecoder())
                .contract(new SpringMvcContract())
                .target(AuthorClient.class, BASE_URL);
    }

    @Test
    public void getAuthorDetails_shouldReturnAuthorDetails_whenResponseOk() {
        String authorName = "JohnDoe";
        String jsonBody = "{\"name\":\"John Doe\",\"biography\":\"Bio\",\"nationality\":\"USA\"}";
        Response response = Response.builder()
                .request(Request.create(Request.HttpMethod.GET,
                        BASE_URL + "/authors/" + authorName,
                        Collections.emptyMap(), null, StandardCharsets.UTF_8, null))
                .status(200)
                .body(jsonBody, StandardCharsets.UTF_8)
                .build();

        AuthorClient client = getClient(response);

        AuthorDetails authorDetails = client.getAuthorDetails(authorName);

        assertNotNull(authorDetails);
        assertEquals("John Doe", authorDetails.name());
        assertEquals("Bio", authorDetails.biography());
        assertEquals("USA", authorDetails.nationality());
    }

    @Test
    public void getAuthorDetails_shouldThrowEntityNotFoundException_whenResponse404() {
        String authorName = "NonExisting";
        Response response = Response.builder()
                .request(Request.create(Request.HttpMethod.GET,
                        BASE_URL + "/authors/" + authorName,
                        Collections.emptyMap(), null, StandardCharsets.UTF_8, null))
                .status(404)
                .body("", StandardCharsets.UTF_8)
                .build();

        AuthorClient client = getClient(response);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> client.getAuthorDetails(authorName));
        assertEquals(AUTHOR_NOT_FOUND, exception.getMessage());
    }

    @Test
    public void getAuthorDetails_shouldThrowAuthorServiceUnavailableException_whenResponse503() {
        String authorName = "AnyAuthor";
        Response response = Response.builder()
                .request(Request.create(Request.HttpMethod.GET,
                        BASE_URL + "/authors/" + authorName,
                        Collections.emptyMap(), null, StandardCharsets.UTF_8, null))
                .status(503)
                .body("", StandardCharsets.UTF_8)
                .build();

        AuthorClient client = getClient(response);

        AuthorServiceUnavailableException exception = assertThrows(AuthorServiceUnavailableException.class, () -> client.getAuthorDetails(authorName));
        assertEquals(AUTHOR_SERVICE_UNAVAILABLE, exception.getMessage());
    }
}
