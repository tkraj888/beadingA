package com.spring.jwt.Color.Dto;

import lombok.Data;

@Data
public class OneColorDto {
    private String name;


    public OneColorDto(String name) {
        this.name = name;
    }
}
