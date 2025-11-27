package com.spring.jwt.controller;

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
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
// Remove or comment out @Controller
// import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

// COMMENT OUT THIS ANNOTATION to disable STOMP WebSocket controller
// @Controller
@RequiredArgsConstructor
public class WebSocketBidController {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketBidController.class);

    private final PlacedBidService placedBidService;
    private final SimpMessagingTemplate messagingTemplate;
    private final BeadingCarService beadingCarService;
    private final BidCarsRepo bidCarsRepo;
    private final BidCarsServiceImpl bidCarsService;

    @PreAuthorize("permitAll")
    @MessageMapping("/placeBid")
    @SendTo("/topic/bids")
    public ResponseDto placeBid(PlacedBidDTO placedBidDTO) {
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
                List<BidCarsDTO> liveCars = beadingCarService.getAllLiveCars();

                messagingTemplate.convertAndSend("/topic/liveCars", liveCars);

                messagingTemplate.convertAndSend("/topic/bids", placedBidDTO);

                List<PlacedBidDTO> topThreeBids = placedBidService.getTopThree(placedBidDTO.getBidCarId());
                messagingTemplate.convertAndSend("/topic/topThreeBids", topThreeBids);

                PlacedBidDTO topBid = placedBidService.getTopBid(placedBidDTO.getBidCarId());
                messagingTemplate.convertAndSend("/topic/topBid", topBid);

                messagingTemplate.convertAndSend("/topic/topBids", topBid);

                return new ResponseDto("success", result);     } else {
                System.err.println("BidCar not found with ID: " + placedBidDTO.getBidCarId());
                return new ResponseDto("error", "BidCar not found with ID: " + placedBidDTO.getBidCarId());
            }
        } catch (BidAmountLessException | UserNotFoundExceptions | BidForSelfAuctionException |
                 InsufficientBalanceException e) {
            logger.error("Error placing bid: {}", e.getMessage());
            return new ResponseDto("error", e.getMessage());
        }
    }

    @MessageMapping("/topThreeBids")
    @SendTo("/topic/topThreeBids")
    public List<PlacedBidDTO> getTopThreeBids(PlacedBidDTO placedBidDTO) {
        try {
            if (placedBidDTO.getBidCarId() == null) {
                throw new IllegalArgumentException("Bid car ID must not be null");
            }
            return placedBidService.getTopThree(placedBidDTO.getBidCarId());
        } catch (IllegalArgumentException e) {
            logger.error("Invalid request for top three bids: {}", e.getMessage());
            return Collections.emptyList();
        } catch (BidNotFoundExceptions e) {
            logger.error("Error finding top three bids: {}", e.getMessage());
            return Collections.emptyList();
        }
    }


    @MessageMapping("/topBid")
    @SendTo("/topic/topBid")
    public PlacedBidDTO getTopBid(PlacedBidDTO placedBidDTO) {
        try {
            return placedBidService.getTopBid(placedBidDTO.getBidCarId());
        } catch (BidNotFoundExceptions e) {
            logger.error("Error finding top bid: {}", e.getMessage());
            return null;
        }
    }

    @MessageMapping("/topBids/{bidCarId}")
    @SendTo("/topic/topBids")
    public PlacedBidDTO getTopBid(@DestinationVariable Integer bidCarId) {
        try {
            return placedBidService.getTopBid(bidCarId);
        } catch (BidNotFoundExceptions e) {
            logger.error("Error finding top bids: {}", e.getMessage());
            return null;
        }
    }

        @PreAuthorize("permitAll")
        @MessageMapping("/liveCars")
        @SendTo("/topic/liveCars")
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

}
