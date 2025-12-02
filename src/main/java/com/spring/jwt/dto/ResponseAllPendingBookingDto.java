package com.spring.jwt.dto;

import com.spring.jwt.premiumCar.PremiumCarPendingBooking.PremiunCarPendingBookingDto;
import lombok.Data;

import java.util.List;

@Data

public class ResponseAllPendingBookingDto {
    private String message;
    private List<PendingBookingDTO> list;
    private List<PremiunCarPendingBookingDto> premiunCarPendingBookingDtos;
    private String exception;

    public ResponseAllPendingBookingDto(String message){
        this.message=message;
    }

}
