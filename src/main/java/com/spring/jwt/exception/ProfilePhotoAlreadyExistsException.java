package com.spring.jwt.exception;

public class ProfilePhotoAlreadyExistsException extends RuntimeException {
    public ProfilePhotoAlreadyExistsException(String message) {
        super(message);
    }
}
