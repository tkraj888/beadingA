package com.spring.jwt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class BidAmountLessException extends Exception {
    public BidAmountLessException(String message) {
        super(message);
    }
}