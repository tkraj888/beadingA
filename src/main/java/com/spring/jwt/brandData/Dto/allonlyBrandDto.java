package com.spring.jwt.brandData.Dto;

import lombok.Data;

import java.util.List;
@Data
public class allonlyBrandDto {
    private String message;
    private List<onlyBrandDto> list;
    private String exception;

    public allonlyBrandDto(String message) {
        this.message = message;
    }

    public allonlyBrandDto(String message, List<onlyBrandDto> list, String exception) {
        this.message = message;
        this.list = list;
        this.exception = exception;
    }
}
