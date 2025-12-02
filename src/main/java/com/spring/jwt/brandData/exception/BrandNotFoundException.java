package com.spring.jwt.brandData.exception;

import org.springframework.http.HttpStatus;

public class BrandNotFoundException extends RuntimeException{

    private final String message;
    private HttpStatus httpStatus;
    public BrandNotFoundException(String message) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
    public String getMessage() {
        return message;
    }
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}

