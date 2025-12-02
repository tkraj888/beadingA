package com.spring.jwt.Color.Dto;

import lombok.Data;

import java.util.List;
@Data
public class AllColorDto {

    private String message;
    private List<OneColorDto> list;
    private String exception;

    public AllColorDto(String message) {
        this.message = message;
    }

    public AllColorDto(String message, List<OneColorDto> list, String exception) {
        this.message = message;
        this.list = list;
        this.exception = exception;
    }
}
