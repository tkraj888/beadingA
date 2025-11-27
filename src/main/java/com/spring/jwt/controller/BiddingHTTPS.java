package com.spring.jwt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.spring.jwt.Interfaces.BeadingCarService;
import com.spring.jwt.Interfaces.PlacedBidService;
import com.spring.jwt.dto.BeedingDtos.PlacedBidDTO;
import com.spring.jwt.dto.BidCarsDTO;
import com.spring.jwt.dto.ResponseDto;
import com.spring.jwt.entity.BidCars;
import com.spring.jwt.exception.*;
import com.spring.jwt.repository.BidCarsRepo;
import com.spring.jwt.service.BidCarsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/biddingHTTP")
public class BiddingHTTPS {
    private static final Logger logger = LoggerFactory.getLogger(BiddingHTTPS.class);

    private final BeadingCarService beadingCarService;
    private final BidCarsServiceImpl bidCarsService;
    private final PlacedBidService placedBidService;
    private final BidCarsRepo bidCarsRepo;

    // Remove these imports:
    // import org.springframework.messaging.handler.annotation.DestinationVariable;
    // import org.springframework.messaging.handler.annotation.MessageMapping;
    // import org.springframework.messaging.handler.annotation.SendTo;
    // import org.springframework.messaging.simp.SimpMessagingTemplate;

    // ... rest of your methods remain the same
    @GetMapping("/liveCars")
    public List<BidCarsDTO> getAllLiveCars() {
        try {
            LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
            List<BidCarsDTO> liveCars = beadingCarService.getAllLiveCars();
            System.err.println(liveCars);
            return liveCars;
        } catch (Exception e) {
            logger.error("Error getting live cars: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    @GetMapping("/liveCar/{bidingCarId}")
    public BidCarsDTO getAllLiveCars(@PathVariable Integer bidingCarId) {
        try {
            LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
            BidCarsDTO liveCars = beadingCarService.getLiveCar(bidingCarId);
            System.err.println(liveCars);
            return liveCars;
        } catch (Exception e) {
            logger.error("Error getting live cars: {}", e.getMessage());
            return null;
        }
    }

    @PostMapping("/placeBidHttp")
    public ResponseDto placeBidHttp(@RequestBody PlacedBidDTO placedBidDTO) {
        try {
            logger.info("Received bid: {}", placedBidDTO);

            Optional<BidCars> bidCarOpt = bidCarsRepo.findById(placedBidDTO.getBidCarId());
            if (bidCarOpt.isPresent()) {
                BidCars bidCar = bidCarOpt.get();
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime closingTime = bidCar.getClosingTime();
                System.out.println("Current Time: " + now);
                System.out.println("Closing Time: " + closingTime);

                if (closingTime.isBefore(now)) {
                    return new ResponseDto("error", "Bidding is over. No more bids can be placed.");
                }

                String result = placedBidService.placeBid(placedBidDTO, placedBidDTO.getBidCarId());

                if (closingTime.isAfter(now) && closingTime.minusMinutes(2).isBefore(now)) {
                    System.out.println("Bid placed within the last 2 minutes. Extending closing time.");
                    bidCar.setClosingTime(closingTime.plusMinutes(2));
                    bidCarsRepo.save(bidCar);

                    bidCarsService.scheduleBidProcessing(bidCar);

                    System.err.println("Updated Closing Time: " + bidCar.getClosingTime());
                } else {
                    System.err.println("Bid not placed within the last 2 minutes. No extension needed.");
                }

                return new ResponseDto("success", result);
            } else {
                System.err.println("BidCar not found with ID: " + placedBidDTO.getBidCarId());
                return new ResponseDto("error", "BidCar not found with ID: " + placedBidDTO.getBidCarId());
            }
        } catch (BidAmountLessException | UserNotFoundExceptions | BidForSelfAuctionException |
                 InsufficientBalanceException e) {
            logger.error("Error placing bid: {}", e.getMessage());
            return new ResponseDto("error", e.getMessage());
        }
    }

    @DeleteMapping("/deleteAll")
    public ResponseDto deleteAllBiddingData(@RequestParam(required = false) String delete) {
        try {
            if (!"1010".equals(delete)) {
                return new ResponseDto("Failed", "Invalid delete key provided", null);
            }

            bidCarsService.deleteallok();
            return new ResponseDto("Success", "All bidding data deleted successfully", null);
        } catch (Exception e) {
            return new ResponseDto("Failed", e.getMessage(), null);
        }
    }
}