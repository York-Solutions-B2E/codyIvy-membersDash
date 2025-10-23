package com.example.benefits.model;

import org.springframework.http.HttpStatus;

public class GoogleAuthException extends RuntimeException {
    private final HttpStatus httpStatus;

    public GoogleAuthException(String message) {
        super(message);
        this.httpStatus = HttpStatus.BAD_GATEWAY;
    }

    public GoogleAuthException(String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = HttpStatus.BAD_GATEWAY;

    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
