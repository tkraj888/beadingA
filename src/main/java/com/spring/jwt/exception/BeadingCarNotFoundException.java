package com.spring.jwt.exception;

import org.springframework.http.HttpStatus;

public class BeadingCarNotFoundException extends RuntimeException{


    private HttpStatus httpStatus;
    public BeadingCarNotFoundException() {


        super("car not found");
    }

    public BeadingCarNotFoundException(String message, HttpStatus httpStatus) {

        super(message);
        this.httpStatus = httpStatus;

    }

    public BeadingCarNotFoundException(String s) {
    }
}
