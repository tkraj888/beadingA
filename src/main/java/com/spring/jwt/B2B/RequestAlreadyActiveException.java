package com.spring.jwt.B2B;

public class RequestAlreadyActiveException extends RuntimeException {
    public RequestAlreadyActiveException(String message)
    {
        super(message);    }
}