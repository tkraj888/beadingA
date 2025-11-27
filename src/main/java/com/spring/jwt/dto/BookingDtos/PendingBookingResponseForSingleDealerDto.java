package com.spring.jwt.dto.BookingDtos;

import com.spring.jwt.dto.PendingBookingDTO;
import com.spring.jwt.premiumCar.PremiumCarDto;
import com.spring.jwt.premiumCar.PremiumCarPendingBooking.PremiunCarPendingBookingDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PendingBookingResponseForSingleDealerDto {
    private String message;
    private PendingBookingDTO pendingBookingDTO;
    private PremiunCarPendingBookingDto premiunCarPendingBookingDto;
    private String exception;

    public PendingBookingResponseForSingleDealerDto(String message) {
        this.message = message;
    }
}
