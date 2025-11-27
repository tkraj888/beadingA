package com.spring.jwt.B2B;

import lombok.Data;

import java.util.List;
@Data
public class ResponseAllB2BDto {
    private String status;

    private String message;

    private List<B2BDto> list;

    private String exception;

}
