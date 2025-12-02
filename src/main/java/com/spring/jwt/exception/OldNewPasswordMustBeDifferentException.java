package com.spring.jwt.exception;

public class OldNewPasswordMustBeDifferentException extends RuntimeException {

    public OldNewPasswordMustBeDifferentException(String message) {
        super(message);
    }
}
