//package com.spring.jwt.utils;
//
//import com.spring.jwt.Interfaces.PlacedBidService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class AuctionScheduler {
//
//    private final PlacedBidService placedBidService;
//
//    @Scheduled(fixedRate = 60000)
//    public void processClosedAuctions() {
//        placedBidService.processClosedAuctions();
//    }
//}
//
