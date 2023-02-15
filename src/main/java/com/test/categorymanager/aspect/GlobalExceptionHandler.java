package com.test.categorymanager.aspect;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = { ResponseStatusException.class })
    protected ResponseEntity<String> handleResponseStatusException(ResponseStatusException responseStatusException) {
        return new ResponseEntity<>(responseStatusException.getMessage(), responseStatusException.getStatusCode());
    }
}
