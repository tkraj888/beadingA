package com.spring.jwt.dto.saveCar;

import lombok.Data;

import java.util.List;

@Data
public class ResponseAllSaveCarDto {
    private String message;
    private List<SaveCarDto> list;
    private String exception;

    public ResponseAllSaveCarDto(String message, List<SaveCarDto> list, String exception) {
        this.message = message;
        this.list = list;
        this.exception = exception;
    }
}
