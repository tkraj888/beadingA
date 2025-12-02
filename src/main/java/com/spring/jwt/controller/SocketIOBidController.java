package com.spring.jwt.controller;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.spring.jwt.Interfaces.BeadingCarService;
import com.spring.jwt.Interfaces.PlacedBidService;
import com.spring.jwt.dto.BeedingDtos.PlacedBidDTO;
import com.spring.jwt.dto.BidCarsDTO;
import com.spring.jwt.dto.ResponseDto;
import com.spring.jwt.entity.BidCars;
import com.spring.jwt.exception.*;
import com.spring.jwt.repository.BidCarsRepo;
import com.spring.jwt.service.BidCarsServiceImpl;
import com.spring.jwt.service.SocketIOService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class SocketIOBidController {

    private final PlacedBidService placedBidService;
    private final SocketIOService socketIOService;
    private final BeadingCarService beadingCarService;
    private final BidCarsRepo bidCarsRepo;
    private final BidCarsServiceImpl bidCarsService;

    @OnConnect
    public void onConnect(SocketIOClient client) {
        log.info("Client connected: {}", client.getSessionId());
    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        log.info("Client disconnected: {}", client.getSessionId());
    }

    @OnEvent("placeBid")
    public void onPlaceBid(SocketIOClient client, PlacedBidDTO placedBidDTO) {
        try {
            log.info("Received bid: {}", placedBidDTO);

            Optional<BidCars> bidCarOpt = bidCarsRepo.findById(placedBidDTO.getBidCarId());
            if (bidCarOpt.isPresent()) {
                BidCars bidCar = bidCarOpt.get();
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime closingTime = bidCar.getClosingTime();
                log.info("Current Time: {}, Closing Time: {}", now, closingTime);

                if (closingTime.isBefore(now)) {
                    ResponseDto errorResponse = new ResponseDto("error", "Bidding is over. No more bids can be placed.");
                    client.sendEvent("placeBidResponse", errorResponse);
                    return;
                }

                String result = placedBidService.placeBid(placedBidDTO, placedBidDTO.getBidCarId());

                if (closingTime.isAfter(now) && closingTime.minusMinutes(2).isBefore(now)) {
                    log.info("Bid placed within the last 2 minutes. Extending closing time.");
                    bidCar.setClosingTime(closingTime.plusMinutes(2));
                    bidCarsRepo.save(bidCar);
                    bidCarsService.scheduleBidProcessing(bidCar);
                    log.info("Updated Closing Time: {}", bidCar.getClosingTime());
                }

                // Send response to client
                ResponseDto successResponse = new ResponseDto("success", result);
                client.sendEvent("placeBidResponse", successResponse);

                // Broadcast updates to all clients
                List<BidCarsDTO> liveCars = beadingCarService.getAllLiveCars();
                socketIOService.sendToAll("liveCars", liveCars);
                socketIOService.sendToAll("bids", placedBidDTO);

                List<PlacedBidDTO> topThreeBids = placedBidService.getTopThree(placedBidDTO.getBidCarId());
                socketIOService.sendToAll("topThreeBids", topThreeBids);

                PlacedBidDTO topBid = placedBidService.getTopBid(placedBidDTO.getBidCarId());
                socketIOService.sendToAll("topBid", topBid);
                socketIOService.sendToAll("topBids", topBid);

            } else {
                log.error("BidCar not found with ID: {}", placedBidDTO.getBidCarId());
                ResponseDto errorResponse = new ResponseDto("error", "BidCar not found with ID: " + placedBidDTO.getBidCarId());
                client.sendEvent("placeBidResponse", errorResponse);
            }
        } catch (BidAmountLessException | UserNotFoundExceptions | BidForSelfAuctionException |
                 InsufficientBalanceException e) {
            log.error("Error placing bid: {}", e.getMessage());
            ResponseDto errorResponse = new ResponseDto("error", e.getMessage());
            client.sendEvent("placeBidResponse", errorResponse);
        }
    }

    @OnEvent("getTopThreeBids")
    public void onGetTopThreeBids(SocketIOClient client, PlacedBidDTO placedBidDTO) {
        try {
            if (placedBidDTO.getBidCarId() == null) {
                throw new IllegalArgumentException("Bid car ID must not be null");
            }
            List<PlacedBidDTO> topThreeBids = placedBidService.getTopThree(placedBidDTO.getBidCarId());
            client.sendEvent("topThreeBids", topThreeBids);
        } catch (IllegalArgumentException e) {
            log.error("Invalid request for top three bids: {}", e.getMessage());
            client.sendEvent("topThreeBids", Collections.emptyList());
        } catch (BidNotFoundExceptions e) {
            log.error("Error finding top three bids: {}", e.getMessage());
            client.sendEvent("topThreeBids", Collections.emptyList());
        }
    }

    @OnEvent("getTopBid")
    public void onGetTopBid(SocketIOClient client, PlacedBidDTO placedBidDTO) {
        try {
            PlacedBidDTO topBid = placedBidService.getTopBid(placedBidDTO.getBidCarId());
            client.sendEvent("topBid", topBid);
        } catch (BidNotFoundExceptions e) {
            log.error("Error finding top bid: {}", e.getMessage());
            client.sendEvent("topBid", null);
        }
    }

    @OnEvent("liveCars")
    public void onGetLiveCars(SocketIOClient client, Object data) {
        try {
            LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
            List<BidCarsDTO> liveCars = beadingCarService.getAllLiveCars();
            log.debug("Sending live cars to client: {}", liveCars);
            client.sendEvent("liveCars", liveCars);
        } catch (Exception e) {
            log.error("Error getting live cars: {}", e.getMessage());
            client.sendEvent("liveCars", Collections.emptyList());
        }
    }
}
