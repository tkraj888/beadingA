package com.spring.jwt.exception;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class TransactionNotFoundException extends RuntimeException {
    private String code;

    private String message;

    public TransactionNotFoundException(String s) {
    }
}
