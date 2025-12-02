package com.spring.jwt.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class ResponseUserProfileDto {
    private String message;
    private UserProfileDto userProfileDto;
    private String exception;

    public ResponseUserProfileDto(String message) {
        this.message=message;
    }
}

