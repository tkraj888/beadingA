package com.spring.jwt.dto;

import com.spring.jwt.premiumCar.PremiumCarDto;
import lombok.Data;

import java.util.List;

@Data
public class ResponseAllCarDto {
    private String message;
    private List<CarDto> list;
    private List<PremiumCarDto> list1;
    private String exception;
    private long totalCars;

    public ResponseAllCarDto(String message){
        this.message=message;
    }
}
