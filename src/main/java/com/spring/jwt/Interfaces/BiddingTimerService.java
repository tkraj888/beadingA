package com.spring.jwt.Interfaces;

import com.spring.jwt.dto.BiddingTimerRequestDTO;

import java.util.List;


public interface BiddingTimerService {

    public BiddingTimerRequestDTO startTimer (BiddingTimerRequestDTO biddingTimerRequest);

//    void sendNotification (String recipient, String message);

    void sendBulkEmails(List<String> recipients, String message);

    BiddingTimerRequestDTO updateBiddingTime(BiddingTimerRequestDTO updateBiddingTimeRequest);

    BiddingTimerRequestDTO getCarByTimerId(Integer biddingTimerId);

//    BiddingTimerRequestDTO updateBiddingTime(BiddingTimerRequestDTO updateBiddingTimeRequest);


}
