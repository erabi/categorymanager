package com.test.categorymanager.aspect.exception;

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

    public String getMessage() {
        return message;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

}
