package com.spring.jwt.Interfaces;

import com.spring.jwt.dto.BookingDtos.PendingBookingRequestDto;
import com.spring.jwt.dto.PendingBookingDTO;

import java.util.List;

public interface PendingBookingService {

    public PendingBookingRequestDto savePendingBooking(PendingBookingDTO pendingBookingDTO);

    public void deleteBooking(int id);

    public void statusUpdate(PendingBookingDTO pendingBookingDTO);

    public List<PendingBookingDTO> getAllPendingBookingWithPage(int PageNo);

    public List<PendingBookingDTO>getAllPendingBookingByUserId(int pageNo, int userId);

    public PendingBookingDTO getPendingBookingId(int bookingId);

    public List<PendingBookingDTO> getPendingBookingsByDealerId(int pageNo, int dealerId);

    public List<PendingBookingDTO> getPendingBookingsByCarId(int pageNo, int carId);
}
