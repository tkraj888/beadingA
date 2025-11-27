package com.spring.jwt.service;

import com.spring.jwt.repository.TempPendingBookingReqRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TempPendingBookingService {

    private final TempPendingBookingReqRepository tempPendingBookingReqRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanUpOldTempBookings() {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(30);
        tempPendingBookingReqRepository.deleteOlderThan(cutoffDate);
    }
}
