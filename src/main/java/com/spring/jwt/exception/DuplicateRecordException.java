package com.spring.jwt.exception;

import org.springframework.http.HttpStatus;

public class DuplicateRecordException extends RuntimeException {
    private HttpStatus status;

    public DuplicateRecordException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
