package com.test.categorymanager.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExceptionResponse {

    private final String message;

    private String exceptionMessage;

    public ExceptionResponse(String message) {
        this.message = message;
    }

    public ExceptionResponse(String message, Exception exceptionSource) {
        this(message);
        if (exceptionSource != null) {
            this.exceptionMessage = exceptionSource.getMessage();
        }
    }
}
