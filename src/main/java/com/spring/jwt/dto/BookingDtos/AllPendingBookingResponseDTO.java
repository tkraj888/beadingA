package com.spring.jwt.dto.BookingDtos;

import com.spring.jwt.dto.PendingBookingDTO;
import com.spring.jwt.premiumCar.PremiumCarPendingBooking.PremiunCarPendingBookingDto;
import lombok.Data;

import java.util.List;

@Data

public class AllPendingBookingResponseDTO {
    private String message;
    private List<PendingBookingDTO> list;
    private List<PremiunCarPendingBookingDto> premiunCarPendingBookingDtos;
    private String exception;

    public AllPendingBookingResponseDTO(String message){
        this.message=message;
    }

}
