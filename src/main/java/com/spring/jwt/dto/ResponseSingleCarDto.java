package com.spring.jwt.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseSingleCarDto {
    private String message;
    private Object object;
    private String exception;

    public ResponseSingleCarDto(String message) {
        this.message = message;
    }

    public ResponseSingleCarDto(String message,String exception, Object object) {
        this.message = message;
        this.object = object;
        this.exception = exception;
    }

    public ResponseSingleCarDto(String success, CarDto car) {
    }

    public ResponseSingleCarDto(String unsuccess, String s) {
    }

    public ResponseSingleCarDto() {

    }

    // Constructor for error response with exception


    // Constructor for simple success message

}
