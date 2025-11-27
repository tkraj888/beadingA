package com.spring.jwt.exception;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class UserAlreadyExistException extends RuntimeException{
    private String code;

    private String message;

    public UserAlreadyExistException(String message) {
        super(message);
        this.message = message;
    }

    // Getters for code and message
    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
