package com.spring.jwt.userForm.Dto;

import lombok.Data;

import java.util.List;
@Data
public class ResponseAllUserFormDto {

    private String message;
    private List<userFormDto> list;
    private String exception;

    public ResponseAllUserFormDto(String message){
        this.message=message;
    }
}


