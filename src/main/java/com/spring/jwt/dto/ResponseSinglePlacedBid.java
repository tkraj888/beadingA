package com.spring.jwt.dto;
import lombok.Data;

@Data
public class ResponseSinglePlacedBid {
    private String message;
    private Object object;
    private String exception;

    public ResponseSinglePlacedBid(String message) {
        this.message = message;
    }

    public ResponseSinglePlacedBid(String message, Object object, String exception) {
        this.message = message;
        this.object = object;
        this.exception = exception;
    }
}


