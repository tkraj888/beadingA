package com.spring.jwt.B2BConfirm;

import lombok.Data;

import java.util.List;
@Data
public class ResponceAllConfirmDto {


    private String message;
    private List<B2BConfirmDto> list;      private String exception;

    // Constructor for initializing the message only
    public ResponceAllConfirmDto(String message) {
        this.message = message;
    }

    // Constructor for initializing message, object, and exception
    public ResponceAllConfirmDto(String message, List<B2BConfirmDto> list, String exception) {
        this.message = message;
        this.list = list;
        this.exception = exception;
    }
}
