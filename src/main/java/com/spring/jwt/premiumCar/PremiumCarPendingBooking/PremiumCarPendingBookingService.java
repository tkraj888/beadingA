package com.spring.jwt.premiumCar.PremiumCarPendingBooking;

import com.spring.jwt.dto.BookingDtos.PendingBookingRequestDto;
import com.spring.jwt.dto.PendingBookingDTO;

import java.util.List;

public interface PremiumCarPendingBookingService {

    PremiunCarPendingBookingDto createPremiumCarPendingBookingService (PremiunCarPendingBookingDto premiunCarPendingBookingDto);

    PremiunCarPendingBookingDto getPendingBookingId(Long bookingId);

    List<PremiunCarPendingBookingDto> getAllPendingBookingWithPage(int pageNo);

    List<PremiunCarPendingBookingDto> getAllPendingBookingByUserId(int pageNo, int userId);

    List<PremiunCarPendingBookingDto> getPendingBookingsByDealerId(int pageNo, int dealerId);

    List<PremiunCarPendingBookingDto> getPendingBookingsByCarId(int pageNo, int carId);

    void statusUpdate(PremiunCarPendingBookingDto pendingBookingDTO);

    void deleteBooking(Long id);

    public PremiumCarPendingBookingRequestDto savePendingBooking(PremiunCarPendingBookingDto pendingBookingDTO);
}
