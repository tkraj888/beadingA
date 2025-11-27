package com.spring.jwt.dto;

import lombok.Data;

import java.util.List;

@Data
public class ResponseForList {
    private String message;
    private List<Object> object;  // Change the type to List of Objects or adjust as per your needs
    private String exception;

    // Constructor for initializing the message only
    public ResponseForList(String message) {
        this.message = message;
    }

    // Constructor for initializing message, object, and exception
    public ResponseForList(String message, List<Object> object, String exception) {
        this.message = message;
        this.object = object;
        this.exception = exception;
    }
}
