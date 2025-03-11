package com.andersenlab.test.andersentest.exception.handler;

import com.andersenlab.test.andersentest.exception.AccessRestrictedException;
import com.andersenlab.test.andersentest.exception.AuthorServiceUnavailableException;
import com.andersenlab.test.andersentest.exception.EntityNotFoundException;
import com.andersenlab.test.andersentest.exception.InvalidInputException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<String> handleInvalidFieldException(final ConstraintViolationException e) {
        log.error(e.getMessage());
        Map<String, List<String>> errorMap = new HashMap<>();
        for (ConstraintViolation<?> value : e.getConstraintViolations()) {
            if (errorMap.containsKey(value.getPropertyPath().toString())) {
                errorMap.get(value.getPropertyPath().toString()).add(value.getMessageTemplate());
            } else {
                errorMap.put(value.getPropertyPath().toString(), new ArrayList<>(List.of(value.getMessageTemplate())));
            }
        }
        return ResponseEntity.badRequest().body(errorMap.toString());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleInvalidDtoException(final MethodArgumentNotValidException e) {
        log.warn(e.getMessage());
        List<Object> messages = Arrays.stream(Objects.requireNonNull(e.getDetailMessageArguments())).toList();
        return ResponseEntity.badRequest().body(messages.get(1).toString());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleEntityNotFoundException(final EntityNotFoundException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<String> handleAccessRestrictedException(final AccessRestrictedException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleInvalidInputException(final InvalidInputException e) {
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(AuthorServiceUnavailableException.class)
    public ResponseEntity<String> handleServiceUnavailable() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Service unavailable, try again later");
    }
}
