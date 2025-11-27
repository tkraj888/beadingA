package com.spring.jwt.Color.Dto;

import lombok.Data;

import java.util.List;
@Data
public class AllColorDto1 {

    private String message;
    private List<?> list;
    private String exception;

    public AllColorDto1(String message) {
        this.message = message;
    }

    public AllColorDto1(String message, List<?> list, String exception) {
        this.message = message;
        this.list = list;
        this.exception = exception;
    }
}
