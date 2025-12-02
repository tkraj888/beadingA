package com.spring.jwt.dto.BookingDtos;

import com.spring.jwt.premiumCar.PremiumCarPendingBooking.PremiumCarPendingBookingRequestDto;
import com.spring.jwt.premiumCar.PremiumCarPendingBooking.PremiunCarPendingBookingDto;
import lombok.Data;

@Data
public class ResponsePendingBookingRequestDto {
    private String message;
    private PendingBookingRequestDto pendingBookingRequestDto;
    private PremiumCarPendingBookingRequestDto premiumCarPendingBookingRequestDto;
    private String exception;

    public ResponsePendingBookingRequestDto(String message) {
        this.message = message;
    }
}
