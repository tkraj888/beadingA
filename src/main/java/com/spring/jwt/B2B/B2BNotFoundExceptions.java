package com.spring.jwt.B2B;

import org.springframework.http.HttpStatus;

public class B2BNotFoundExceptions extends RuntimeException{

    private HttpStatus httpStatus;


    public B2BNotFoundExceptions() {
    }

    public B2BNotFoundExceptions(String s) {
        super(s);
    }

    public B2BNotFoundExceptions(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;

    }}

