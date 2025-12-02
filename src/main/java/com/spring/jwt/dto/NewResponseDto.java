package com.spring.jwt.dto;

import lombok.Data;

@Data
public class NewResponseDto {
    public String message;
    public Object object;
    public Exception exception;

    public NewResponseDto(String message, Object object, Exception exception) {
        this.message = message;
        this.object = object;
        this.exception = exception;
    }



}
