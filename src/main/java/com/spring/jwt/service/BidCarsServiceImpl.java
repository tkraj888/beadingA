package com.spring.jwt.service;

import com.spring.jwt.Interfaces.BidCarsService;
import com.spring.jwt.dto.BidCarsDTO;
import com.spring.jwt.dto.BidDetailsDTO;
import com.spring.jwt.entity.*;
import com.spring.jwt.exception.BeadingCarNotFoundException;
import com.spring.jwt.exception.UserNotFoundExceptions;
import com.spring.jwt.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BidCarsServiceImpl implements BidCarsService {

    private final ModelMapper modelMapper;

    private final BeadingCarRepo beadingCarRepo;

    private final BidCarsRepo bidCarsRepo;

    private final DealerRepository dealerRepository;


    private final PlacedBidRepo placedBidRepo;

    private final FinalBidRepository finalBidRepo;

    private final UserRepository userRepository;

    private final ThreadPoolTaskScheduler taskScheduler;

    private final SocketIOService socketIOService;

    private final BiddingTImerRepo biddingTimerRepo;

    private ScheduledFuture<?> scheduledFuture;

    private static final Logger log = LoggerFactory.getLogger(BidCarsServiceImpl.class);

    public List<BidCarsDTO> getAllLiveCars() {
        LocalDateTime currentTime = LocalDateTime.now();
        List<BidCars> liveCars = bidCarsRepo.findAllLiveCars(currentTime);
        return liveCars.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public BidCarsDTO createBidding(BidCarsDTO bidCarsDTO) {
        System.out.println("Inside ");
        User byUserId = userRepository.findByUserId(bidCarsDTO.getUserId());
        Optional<BidCars> biddingCar = bidCarsRepo.findByBeadingCarId(bidCarsDTO.getBeadingCarId());

        if (biddingCar.isPresent()) {
            throw new RuntimeException("Car Already Added for the Bidding");
        }
        if (byUserId == null) {
            throw new UserNotFoundExceptions("User not found");
        }
        Set<Role> roles = byUserId.getRoles();
        boolean isAuthorized = roles.stream().anyMatch(role -> "SALESPERSON".equals(role.getName()) || "ADMIN".equals(role.getName()));
        if (!isAuthorized) {
            throw new RuntimeException("You're not authorized to perform this action");
        }
        Optional<BeadingCAR> byId = beadingCarRepo.findById(bidCarsDTO.getBeadingCarId());
        if (byId.isEmpty()) {
            throw new RuntimeException("Car Not Found");
        }

        BeadingCAR beadingCAR = byId.get();
        String carStatus = beadingCAR.getCarStatus();
        if (!"ACTIVE".equals(carStatus)) {
            throw new RuntimeException("Car is not Verified by SalesInspector, it can't be bid on.");
        }

        BidCars bidCars = convertToEntity(bidCarsDTO);
        Integer dealerId = beadingCarRepo.findById(bidCarsDTO.getBeadingCarId()).get().getDealerId();

        Integer id = dealerRepository.findById(dealerId).get().getUser().getId();
        bidCars.setUserId(id);
        BidCars savedBid = bidCarsRepo.save(bidCars);

        scheduleBidProcessing(savedBid);

        notifyLiveCarsUpdate("create", savedBid.getBidCarId());

        return convertToDto(savedBid);
    }

    public void scheduleBidProcessing(BidCars bidCar) {
        LocalDateTime closingTime = bidCar.getClosingTime();
        ZonedDateTime indiaTime = closingTime.atZone(ZoneId.of("Asia/Kolkata"));
        long delay = java.time.Duration.between(ZonedDateTime.now(ZoneId.of("Asia/Kolkata")), indiaTime).toMillis();

        if (delay > 0) {
            if (scheduledFuture != null && !scheduledFuture.isDone()) {
                scheduledFuture.cancel(false);
            }

            scheduledFuture = taskScheduler.schedule(() -> {
                try {
                    log.info("Processing bid for car: " + bidCar.getBidCarId());
                    processBid(bidCar);
                } catch (Exception e) {
                    log.error("Failed to process bid for car: " + bidCar.getBidCarId(), e);
                    retryProcessingBid(bidCar, 3);
                }
            }, new Date(System.currentTimeMillis() + delay));

            log.info("Scheduled task for bidCarId: " + bidCar.getBidCarId() + " with delay: " + delay + " ms");
        }
    }

    private void retryProcessingBid(BidCars bidCar, int retryCount) {
        if (retryCount <= 0) {
            log.error("Exceeded retry limit for car: " + bidCar.getBidCarId());
            return;
        }
        try {
            log.info("Retrying bid processing for car: " + bidCar.getBidCarId());
            processBid(bidCar);

        } catch (Exception e) {
            log.error("Retry failed for car: " + bidCar.getBidCarId(), e);
            retryProcessingBid(bidCar, retryCount - 1);
        }
    }

    public void processBid(BidCars bidCar) {
        log.info("Executing processBid for car: {}", bidCar.getBidCarId());

        try {
            List<PlacedBid> highestBids = placedBidRepo.findTopBidByBidCarId(bidCar.getBidCarId(), PageRequest.of(0, 1));
            if (!highestBids.isEmpty()) {
                PlacedBid bid = highestBids.get(0);
                if (bid != null && bidCar != null) {
                    FinalBid finalBid = new FinalBid();

                    finalBid.setSellerDealerId(bidCar.getUserId());
                    finalBid.setBuyerDealerId(bid.getUserId());
                    finalBid.setBidCarId(bidCar.getBidCarId());
                    finalBid.setPrice(bid.getAmount());
                    finalBidRepo.save(finalBid);
                    BeadingCAR beadingCAR = beadingCarRepo.findById(bidCar.getBeadingCarId()).orElse(null);

                    if (beadingCAR!= null){
                        beadingCAR.setCarStatus("SOLD");
                        beadingCarRepo.save(beadingCAR);
                        log.info("Successfully updated status to SOLD for car: {}", bidCar.getBidCarId());
                    }
                notifyLiveCarsUpdate("processBid", bidCar.getBidCarId());
                    log.info("Successfully saved final bid for car: {} with amount: {}", bidCar.getBidCarId(), bid.getAmount());
                } else {
                    log.warn("Invalid bid or bidCar data for car: {}", bidCar.getBidCarId());
                }
            } else {
                log.info("No bids found for car: {}", bidCar.getBidCarId());
            }
        } catch (Exception e) {
            log.error("Error processing bid for car: {}", bidCar.getBidCarId(), e);
        }
    }

    private void notifyLiveCarsUpdate(String source, Integer bidCarId) {
        try {
            List<BidCarsDTO> liveCars = getAllLiveCars();
            socketIOService.sendToAll("liveCars", liveCars);
            log.info("Live cars broadcast triggered after {} for bidCarId={}", source, bidCarId);
        } catch (Exception e) {
            log.error("Failed to broadcast live cars after {} for bidCarId={}", source, bidCarId, e);
        }
    }

    @Override
    public BidDetailsDTO getbyBidId(Integer bidCarId, Integer beadingCarId) {
        Optional<BidCars> bidCarOptional  = bidCarsRepo.findById(bidCarId);
        Optional<BeadingCAR> beadingCarOptional  = beadingCarRepo.findById(beadingCarId);

        if (bidCarOptional.isPresent() && beadingCarOptional.isPresent()) {
            BidCars bidCar = bidCarOptional.get();
            BeadingCAR beadingCar = beadingCarOptional.get();

            BidDetailsDTO bidDetailsDTO = new BidDetailsDTO();

            bidDetailsDTO.setBidCarId(bidCar.getBidCarId());
            bidDetailsDTO.setBeadingCarId(beadingCar.getBeadingCarId());
            bidDetailsDTO.setClosingTime(bidCar.getClosingTime());
            bidDetailsDTO.setCreatedAt(bidCar.getCreatedAt());

            // Set additional features from BeadingCAR
            bidDetailsDTO.setMusicFeature(beadingCar.getMusicFeature());
            bidDetailsDTO.setArea(beadingCar.getArea());
            bidDetailsDTO.setBrand(beadingCar.getBrand());
            bidDetailsDTO.setCarInsurance(beadingCar.getCarInsurance());
            bidDetailsDTO.setCarStatus(beadingCar.getCarStatus());
            bidDetailsDTO.setCity(beadingCar.getCity());
            bidDetailsDTO.setColor(beadingCar.getColor());
            bidDetailsDTO.setDescription(beadingCar.getDescription());
            bidDetailsDTO.setFuelType(beadingCar.getFuelType());
            bidDetailsDTO.setKmDriven(beadingCar.getKmDriven());
            bidDetailsDTO.setModel(beadingCar.getModel());
            bidDetailsDTO.setOwnerSerial(beadingCar.getOwnerSerial());
            bidDetailsDTO.setPowerWindowFeature(beadingCar.getPowerWindowFeature());
            bidDetailsDTO.setPrice(beadingCar.getPrice());
            bidDetailsDTO.setRearParkingCameraFeature(beadingCar.getRearParkingCameraFeature());
            bidDetailsDTO.setRegistration(beadingCar.getRegistration());
            bidDetailsDTO.setTransmission(beadingCar.getTransmission());
            bidDetailsDTO.setYear(beadingCar.getYear());
            bidDetailsDTO.setDate(beadingCar.getDate());
            bidDetailsDTO.setUserId(beadingCar.getUserId());
            bidDetailsDTO.setVariant(beadingCar.getVariant());
            bidDetailsDTO.setTitle(beadingCar.getTitle());
            bidDetailsDTO.setDealer_id(beadingCar.getDealerId());
            bidDetailsDTO.setSunroof(beadingCar.getSunroof());
            bidDetailsDTO.setAirbag(beadingCar.getAirbag());
            bidDetailsDTO.setABS(beadingCar.getABS());
            bidDetailsDTO.setButtonStart(beadingCar.getButtonStart());
            bidDetailsDTO.setChildSafetyLocks(beadingCar.getChildSafetyLocks());

            return bidDetailsDTO;
        } else {
            throw new RuntimeException("Bid car or Beading car not found");
        }
    }

//    public BidDetailsDTO getbyBidId(Integer bidCarId, Integer beadingCarId) {
//        Optional<BidCars> bidCarOptional  = bidCarsRepo.findById(bidCarId);
//        Optional<BeadingCAR> beadingCarOptional  = beadingCarRepo.findById(beadingCarId);
//
//        if (bidCarOptional.isPresent() && beadingCarOptional.isPresent()) {
//            BidCars bidCar = bidCarOptional.get();
//            BeadingCAR beadingCar = beadingCarOptional.get();
//
//            BidDetailsDTO bidDetailsDTO = new BidDetailsDTO();
//
//            bidDetailsDTO.setBidCarId(bidCar.getBidCarId());
//            bidDetailsDTO.setBeadingCarId(beadingCar.getBeadingCarId());
//            bidDetailsDTO.setClosingTime(bidCar.getClosingTime());
//            bidDetailsDTO.setCreatedAt(bidCar.getCreatedAt());
//            bidDetailsDTO.setMusicFeature(beadingCar.getMusicFeature());
//            bidDetailsDTO.setArea(beadingCar.getArea());
//            bidDetailsDTO.setBrand(beadingCar.getBrand());
//            bidDetailsDTO.setCarInsurance(beadingCar.getCarInsurance());
//            bidDetailsDTO.setCarStatus(beadingCar.getCarStatus());
//            bidDetailsDTO.setCity(beadingCar.getCity());
//            bidDetailsDTO.setColor(beadingCar.getColor());
//            bidDetailsDTO.setDescription(beadingCar.getDescription());
//            bidDetailsDTO.setFuelType(beadingCar.getFuelType());
//            bidDetailsDTO.setKmDriven(beadingCar.getKmDriven());
//            bidDetailsDTO.setModel(beadingCar.getModel());
//            bidDetailsDTO.setOwnerSerial(beadingCar.getOwnerSerial());
//            bidDetailsDTO.setPowerWindowFeature(beadingCar.getPowerWindowFeature());
//            bidDetailsDTO.setPrice(beadingCar.getPrice());
//            bidDetailsDTO.setRearParkingCameraFeature(beadingCar.getRearParkingCameraFeature());
//            bidDetailsDTO.setRegistration(beadingCar.getRegistration());
//            bidDetailsDTO.setTransmission(beadingCar.getTransmission());
//            bidDetailsDTO.setYear(beadingCar.getYear());
//            bidDetailsDTO.setDate(beadingCar.getDate());
//            bidDetailsDTO.setUserId(beadingCar.getUserId());
//            bidDetailsDTO.setVariant(beadingCar.getVariant());
//            bidDetailsDTO.setTitle(beadingCar.getTitle());
//            bidDetailsDTO.setDealer_id(beadingCar.getDealerId());
//
//            return bidDetailsDTO;
//        }else {
//            throw new RuntimeException("Bid car or Beading car not found");
//        }
//    }

    @Override
    public List<BidCarsDTO> getByUserId(Integer userId) {
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new UserNotFoundExceptions("User with ID: " + userId + " not found", HttpStatus.NOT_FOUND);
        }

        List<BeadingCAR> beadingCars = beadingCarRepo.findByUserId(userId);
        if (beadingCars.isEmpty()) {
            throw new BeadingCarNotFoundException("No Beading cars found for user with ID: " + userId, HttpStatus.NOT_FOUND);
        }

        List<BidCarsDTO> dtos = new ArrayList<>();
        for (BeadingCAR beadingCAR : beadingCars) {
            dtos.add(new BidCarsDTO(beadingCAR));
        }
        return dtos;
    }

    @Override
    public BidCarsDTO getbyBidId(Integer beadingCarId) {
        Optional<BidCars> byBeadingCarId = bidCarsRepo.findByBeadingCarId(beadingCarId);
        if(byBeadingCarId.isPresent()) {
            BidCars bidCars = byBeadingCarId.get();
            BidCarsDTO carsDTO = convertToDto(bidCars);
            return carsDTO;
        }else{
            throw new RuntimeException("Car Not Found");
        }
    }

    @Override
    public void deleteallok() {

        try {
            // Delete from dependent/secondary table first (BiddingTimer)
            biddingTimerRepo.deleteAll();

            // Then delete from main table (BidCars)
            bidCarsRepo.deleteAll();

        } catch (Exception e) {
            throw new RuntimeException("Error while deleting bidding data: " + e.getMessage());
        }
    }


    public BidCars convertToEntity(BidCarsDTO bidCarsDTO){
        BidCars bdCarEntity = modelMapper.map(bidCarsDTO, BidCars.class);
        return bdCarEntity;
    }

    public BidCarsDTO convertToDto (BidCars bidCars){
        BidCarsDTO bdCarDto = modelMapper.map(bidCars, BidCarsDTO.class);
        return bdCarDto;
    }
}
