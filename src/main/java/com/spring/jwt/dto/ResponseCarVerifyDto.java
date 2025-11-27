package com.spring.jwt.dto;

import lombok.Data;

import java.util.List;
@Data
public class ResponseCarVerifyDto {

        private String message;
        private List<CarVerifyDto> list;
        private String exception;

        public ResponseCarVerifyDto(String message){
            this.message=message;
        }
    }


