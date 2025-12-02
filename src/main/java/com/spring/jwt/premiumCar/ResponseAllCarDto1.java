package com.spring.jwt.premiumCar;
import lombok.Data;

import java.util.List;

@Data
public class ResponseAllCarDto1 {
    private String message;
    private List<PremiumCarDto> list;
    private String exception;
    private long totalCars;

    public ResponseAllCarDto1(String message){
        this.message=message;
    }
}
