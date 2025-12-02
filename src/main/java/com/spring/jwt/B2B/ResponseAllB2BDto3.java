package com.spring.jwt.B2B;

import lombok.Data;

import java.util.List;
@Data
public class ResponseAllB2BDto3 {
    private String status;

    private String message;

    private List<B2BByerGetInfoDto> list;

    private String exception;

}
