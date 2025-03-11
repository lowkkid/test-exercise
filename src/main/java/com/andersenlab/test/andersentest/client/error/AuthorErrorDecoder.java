package com.andersenlab.test.andersentest.client.error;

import com.andersenlab.test.andersentest.exception.AuthorServiceUnavailableException;
import com.andersenlab.test.andersentest.exception.EntityNotFoundException;
import feign.Response;
import feign.codec.ErrorDecoder;

import static com.andersenlab.test.andersentest.constants.ErrorMessages.AUTHOR_NOT_FOUND;
import static com.andersenlab.test.andersentest.constants.ErrorMessages.AUTHOR_SERVICE_UNAVAILABLE;

public class AuthorErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        return switch (response.status()) {
            case 404 -> new EntityNotFoundException(AUTHOR_NOT_FOUND);
            case 503, 504 -> new AuthorServiceUnavailableException(AUTHOR_SERVICE_UNAVAILABLE);
            default -> defaultDecoder.decode(methodKey, response);
        };
    }
}