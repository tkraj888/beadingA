package com.spring.jwt.brandData.Dto;

import lombok.Data;

import java.util.List;

@Data
public class AllBrandDataDto {
    private String message;
    private List<?> list;
    private String exception;

    public AllBrandDataDto(String message) {
        this.message = message;
    }

    public AllBrandDataDto(String message, List<?> list, String exception) {
        this.message = message;
        this.list = list;
        this.exception = exception;
    }
}
