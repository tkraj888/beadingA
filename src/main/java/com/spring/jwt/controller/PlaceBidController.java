package com.spring.jwt.controller;

import com.spring.jwt.Interfaces.BiddingTimerService;
import com.spring.jwt.Interfaces.PlacedBidService;
import com.spring.jwt.dto.*;
import com.spring.jwt.dto.BeedingDtos.PlacedBidDTO;
import com.spring.jwt.entity.BidCars;
import com.spring.jwt.exception.*;
import com.spring.jwt.repository.BidCarsRepo;
import com.spring.jwt.service.BidCarsServiceImpl;
import com.spring.jwt.service.PlacedBidServiceImpl;
import com.spring.jwt.utils.BaseResponseDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/Bid")
@RequiredArgsConstructor
public class PlaceBidController {

    private final PlacedBidService placedBidService;

    private final BidCarsRepo bidCarsRepo;

    private final BiddingTimerService biddingTimerService;

    private final BidCarsServiceImpl bidCarsService;

    private final PlacedBidServiceImpl placedBidServiceImpl;

    private static final Logger logger = LoggerFactory.getLogger(PlaceBidController.class);


    @PostMapping("/placeBid")
    private ResponseEntity<?> placeBid(@RequestBody PlacedBidDTO placedBidDTO, @RequestParam Integer bidCarId) {
        try {
            String result = placedBidService.placeBid(placedBidDTO, bidCarId);

            Optional<BidCars> bidCarOpt = bidCarsRepo.findById(bidCarId);
            if (bidCarOpt.isPresent()) {
                BidCars bidCar = bidCarOpt.get();
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime closingTime = bidCar.getClosingTime();
                System.out.println("Current Time: " + now);
                System.out.println("Closing Time: " + closingTime);

                if (closingTime.isBefore(now)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bidding is over. No more bids can be placed.");
                }

                if (closingTime.isAfter(now) && closingTime.minusMinutes(2).isBefore(now)) {
                    System.out.println("Bid placed within the last 2 minutes. Extending closing time.");
                    bidCar.setClosingTime(closingTime.plusMinutes(2));
                    bidCarsRepo.save(bidCar);
                    bidCarsService.scheduleBidProcessing(bidCar);

                    System.out.println("Updated Closing Time: " + bidCar.getClosingTime());
                } else {
                    System.out.println("Bid not placed within the last 2 minutes. No extension needed.");
                }
            } else {
                System.out.println("BidCar not found with ID: " + bidCarId);
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ResponseAllPlacedBidDTO> getPlacedBidsByUserId(@PathVariable Integer userId) {
        List<PlacedBidDTO> placedBids = null;
        try {
            placedBids = placedBidService.getByUserId(userId);
            return ResponseEntity.ok(new ResponseAllPlacedBidDTO("Placed bids retrieved successfully", placedBids, null));
        } catch (UserNotFoundExceptions e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseAllPlacedBidDTO(e.getMessage(), null, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseAllPlacedBidDTO("An error occurred: " + e.getMessage(), null, null));
        }
    }

    @GetMapping("/{placedBidId}")
    public ResponseEntity<ResponseSinglePlacedBid> getPlacedBidById(@PathVariable Integer placedBidId) {
        try {
            PlacedBidDTO placedBid = placedBidService.getById(placedBidId);
            return ResponseEntity.ok(new ResponseSinglePlacedBid("Placed bid with ID " + placedBidId + " retrieved successfully", placedBid, null));
        } catch (PlacedBidNotFoundExceptions e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseSinglePlacedBid(e.getMessage(), null, null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseSinglePlacedBid("An error occurred", null, e.getMessage()));

        }
    }

    @GetMapping("/topThree/{bidCarId}")
    public ResponseEntity<ResponseAllPlacedBidDTO> getTopThreeBids(@PathVariable Integer bidCarId) {
        try {
            List<PlacedBidDTO> topThreeBids = placedBidService.getTopThree(bidCarId);
            return ResponseEntity.ok(new ResponseAllPlacedBidDTO("Top three bids for car ID " + " retrieved successfully", null, null));
        } catch (BidNotFoundExceptions e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseAllPlacedBidDTO(e.getMessage(), null, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseAllPlacedBidDTO("An error occurred: " + e.getMessage(), null, null));
        }
    }

    @GetMapping("/finalBids")
    public ResponseEntity<ResponseFinalBidDto> getAllFinalBids() {
        try {
            List<FinalBidDto> finalBids = placedBidService.getAllFinalBids();
            return ResponseEntity.ok(new ResponseFinalBidDto("Top three bids for car ID " + " retrieved successfully", finalBids, null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseFinalBidDto("An error occurred: " + e.getMessage(), null, null));
        }
    }


    @GetMapping("getFinalBidById")
    public ResponseEntity<?> getfinalbidById(final Integer bidCarId) {
        try {
            FinalBidDto finalBidDto = placedBidService.getFinalbidById(bidCarId).getBody();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponceDto("success", finalBidDto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponceDto("unsuccess", "Data Not Found for specified Id"));
        }
    }
    @GetMapping("getliveValue")
    public ResponseEntity<?> getfinalbidByIdLive(final Integer bidCarId) {
        try {
            BidPriceDto  finalBidDto = placedBidServiceImpl.getTopBidPrice(bidCarId);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponceDto("success", finalBidDto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponceDto("unsuccess", "Data Not Found for specified Id"));
        }
    }


    @GetMapping("/getAllDealerFinalBids")
    public ResponseEntity<ResponseFinalBidsAll> getAllDealer(@RequestParam Integer buyerDealerId, @RequestParam(value = "pageNo") int pageNo,
                                                             @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        try {
            logger.debug("Received request to fetch all dealer final bids for buyerDealerId: {}", buyerDealerId);
            Page<FinalBidDto> getbids = placedBidService.getDealerAllBids(buyerDealerId, pageNo, pageSize);
            ResponseFinalBidsAll response = new ResponseFinalBidsAll("Success");
            response.setFinalBids(getbids.getContent());
            response.setTotalPages(getbids.getTotalPages());

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (RuntimeException e) {
            logger.error("Error fetching dealer final bids for buyerDealerId: " + buyerDealerId, e);
            ResponseFinalBidsAll response = new ResponseFinalBidsAll("Unsuccessful");
            response.setException(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/car/{bidCarId}")
    public ResponseEntity<ResponseAllPlacedBidDTO> getPlacedBidsByCarId(@PathVariable Integer bidCarId) {
        try {
            List<PlacedBidDTO> placedBids = placedBidService.getByCarID(bidCarId);
            return ResponseEntity.ok(new ResponseAllPlacedBidDTO("Placed bids for car ID " + bidCarId + " retrieved successfully", placedBids, null));
        } catch (BidNotFoundExceptions e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseAllPlacedBidDTO(e.getMessage(), null, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseAllPlacedBidDTO("An error occurred: " + e.getMessage(), null, null));
        }
    }

    @GetMapping("getTimer")
    public ResponseEntity<?> getTimer(@RequestParam Integer biddingTimerId) {
        try {
            BiddingTimerRequestDTO carByTimerId = biddingTimerService.getCarByTimerId(biddingTimerId);
            return ResponseEntity.ok(carByTimerId);
        } catch(BeadingCarNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseResponseDTO("Unsuccessful","Data not found"));
        }
    }
}