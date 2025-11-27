package com.spring.jwt.B2B;

import lombok.Data;

import java.util.List;
@Data
public class ResponseAllB2BDto2 {
    private String status;

    private String message;

    private List<B2BPostDto> list;

    private String exception;

}
