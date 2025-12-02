package com.spring.jwt.exception;


public class SaveCarAlreadyExistsException extends RuntimeException {
    public SaveCarAlreadyExistsException(String message) {
        super(message);
    }
}
