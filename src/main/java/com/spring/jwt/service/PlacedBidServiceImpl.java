package com.spring.jwt.service;

import com.spring.jwt.Interfaces.PlacedBidService;
import com.spring.jwt.Wallet.Entity.WalletAccount;
import com.spring.jwt.Wallet.Repo.AccountRepository;
import com.spring.jwt.dto.BeedingDtos.PlacedBidDTO;
import com.spring.jwt.dto.BidPriceDto;
import com.spring.jwt.dto.BiddingTimerRequestDTO;
import com.spring.jwt.dto.FinalBidDto;
import com.spring.jwt.entity.*;
import com.spring.jwt.exception.*;
import com.spring.jwt.repository.BidCarsRepo;
import com.spring.jwt.repository.FinalBidRepository;
import com.spring.jwt.repository.PlacedBidRepo;
import com.spring.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import com.spring.jwt.service.SocketIOService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlacedBidServiceImpl implements PlacedBidService {
    private final PlacedBidRepo placedBidRepo;

    private final  BidCarsRepo bidCarsRepo;

    private final SocketIOService socketIOService;

    private final ModelMapper modelMapper;

    private final FinalBidRepository finalBidRepo;

    private final AccountRepository accountRepository;

    private final UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(PlacedBidServiceImpl.class);



    @Override
    public String placeBid(PlacedBidDTO placedBidDTO, Integer bidCarId) throws BidAmountLessException, BidForSelfAuctionException {
        Optional<BidCars> carbyId = bidCarsRepo.findById(bidCarId);

        User byUserId = userRepository.findByUserId(placedBidDTO.getUserId());

        if (byUserId== null) {
            throw new UserNotFoundExceptions("User Not Found By Id "+ placedBidDTO.getUserId());
        }

//        Optional<WalletAccount> accountbalance = accountRepository.findByUserId(placedBidDTO.getUserId());
//
//        if (accountbalance.isEmpty()) {
//            throw new UserNotFoundExceptions("Account balance not found for user: " + placedBidDTO.getUserId());
//        }
//        WalletAccount accountBalance = accountbalance.get();
//        if (accountBalance.getOpeningBalance() <= 2000) {
//            throw new InsufficientBalanceException("Minimum Balance for placing bid should be greater than 2000");
//        }

        if (carbyId.isEmpty()){
            throw new UserNotFoundExceptions("Bid Cannot Be Placed as Car is Not Found in Our Database");
        }
        BidCars bidCar = carbyId.get();

        if (bidCar.getUserId().equals(placedBidDTO.getUserId())) {
            throw new BidForSelfAuctionException("You cannot place a bid on your own car");
        }
        PlacedBid placedBid = convertToEntity(placedBidDTO);
        if(placedBid.getAmount()< carbyId.get().getBasePrice()){

            throw new BidAmountLessException("Bid amount cannot be less than base price");
        }

        placedBid.setBidCarId(bidCarId);
        placedBidRepo.save(placedBid);

        return "Bid Placed Successfully";
    }

//    @Override
//    public void processClosedAuctions() {
//        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
//        List<BidCars> closedBidCars = bidCarsRepo.findByClosingTimeBefore(now);
//
//        for (BidCars bidCar : closedBidCars) {
//            // Check if a FinalBid already exists for this bidCarId
//            if (finalBidRepo.existsByBidCarId(bidCar.getBidCarId())) {
//                continue; // Skip to the next bidCar if a FinalBid already exists
//            }
//
//            List<PlacedBid> highestBids = placedBidRepo.findTopBidByBidCarId(bidCar.getBidCarId(), PageRequest.of(0, 1));
//
//            if (!highestBids.isEmpty()) {
//                PlacedBid bid = highestBids.get(0);
//                FinalBid finalBid = new FinalBid();
//                finalBid.setSellerDealerId(bidCar.getUserId());
//                finalBid.setBuyerDealerId(bid.getUserId());
//                finalBid.setBidCarId(bidCar.getBidCarId());
//                finalBid.setPrice(bid.getAmount());
//
//                finalBidRepo.save(finalBid);
//            }
//        }
//    }


    @Override
    public List<PlacedBidDTO> getByUserId(Integer userId) throws UserNotFoundExceptions {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundExceptions("User not found with ID: " + userId);
        }
        List<PlacedBid> bids = placedBidRepo.findByUserId(userId);
        return bids.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlacedBidDTO> getByCarID(Integer bidCarId) throws BidNotFoundExceptions {
        Optional<BidCars> bidCar = bidCarsRepo.findById(bidCarId);
        if (bidCar.isEmpty()) {
            throw new BidNotFoundExceptions("Bid car not found with ID: " + bidCarId);
        }
        List<PlacedBid> bids = placedBidRepo.findByBidCarId(bidCarId);
        return bids.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public PlacedBidDTO getById(Integer placedBidId) throws PlacedBidNotFoundExceptions {
        Optional<PlacedBid> optionalPlacedBid = placedBidRepo.findById(placedBidId);
        if (optionalPlacedBid.isPresent()) {
            PlacedBid placedBid = optionalPlacedBid.get();
            System.out.println(placedBid);
            return convertToDto(placedBid);
        } else {
            throw new PlacedBidNotFoundExceptions("PlacedBid not found with ID: " + placedBidId);
        }
    }

    @Override
    public List<PlacedBidDTO> getTopThree(Integer bidCarId) throws BidNotFoundExceptions {
        Optional<BidCars> bidCarOptional = bidCarsRepo.findById(bidCarId);
        System.out.println(bidCarOptional);
        if (bidCarOptional.isEmpty()) {
            throw new BidNotFoundExceptions("Bid car not found with ID: " + bidCarId);
        }
        BidCars bidCar = bidCarOptional.get();
        List<PlacedBid> topThreeBids = placedBidRepo.findTop3ByBidCarIdOrderByAmountDesc(bidCarId);
        System.err.println(topThreeBids);
        if (topThreeBids.isEmpty()) {
            PlacedBidDTO basePriceBidDTO = new PlacedBidDTO();
            basePriceBidDTO.setBidCarId(bidCarId);
            basePriceBidDTO.setAmount(bidCar.getBasePrice());
            System.err.println("Base price: " + bidCar.getBasePrice());
            return Collections.singletonList(basePriceBidDTO);
        }else {
            return topThreeBids.stream().map(this::convertToDto).collect(Collectors.toList());
        }

    }

    @Override
    public PlacedBidDTO getTopBid(Integer bidCarId) {
        Optional<PlacedBid> topBidOptional = placedBidRepo.findTopByBidCarIdOrderByAmountDesc(bidCarId);

        if (topBidOptional.isPresent()) {
            PlacedBid topBid = topBidOptional.get();

            PlacedBidDTO topBidDTO = new PlacedBidDTO(
                    topBid.getPlacedBidId(),
                    topBid.getUserId(),
                    topBid.getBidCarId(),
                    topBid.getDateTime(),
                    topBid.getAmount()
            );

            sendTopBidUpdate(topBidDTO);

            return topBidDTO;
        } else {
            BidCars car = bidCarsRepo.findById(bidCarId)
                    .orElseThrow(() -> new BidNotFoundExceptions("Car not found for ID: " + bidCarId));
            System.err.println("BasePrice for" + " " + car.getBasePrice());
            PlacedBidDTO basePriceDTO = new PlacedBidDTO();
            basePriceDTO.setBidCarId(car.getBeadingCarId());
            basePriceDTO.setAmount(car.getBasePrice());

            sendTopBidUpdate(basePriceDTO);

            return basePriceDTO;
        }
    }
    @Override
    public BidPriceDto getTopBidPrice(Integer bidCarId) {
        // Get highest bid
        Optional<PlacedBid> topBidOptional = placedBidRepo.findTopByBidCarIdOrderByAmountDesc(bidCarId);

        if (topBidOptional.isPresent()) {
            PlacedBid topBid = topBidOptional.get();

            // Only price and bidCarId
            BidPriceDto topBidDTO = new BidPriceDto(topBid.getBidCarId(), topBid.getAmount());

            return topBidDTO;

        } else {
            // If no bid yet, return base price
            BidCars car = bidCarsRepo.findById(bidCarId)
                    .orElseThrow(() -> new BidNotFoundExceptions("Car not found for ID: " + bidCarId));

            BidPriceDto basePriceDTO = new BidPriceDto(car.getBeadingCarId(), car.getBasePrice());
            return basePriceDTO;
        }
    }


    public void sendTopBidUpdate(PlacedBidDTO bid) {
        logger.info("Publishing top bid update: " + bid);
        // Send to room based on bidCarId
        socketIOService.sendToRoom(String.valueOf(bid.getBidCarId()), "topBids", bid);
        // Also broadcast to all clients
        socketIOService.sendToAll("topBids", bid);
    }

    @Override
    public Page<FinalBidDto> getDealerAllBids(Integer buyerDealerId, int pageNo, int pageSize) {
        logger.debug("Fetching bids for buyerDealerId: {}", buyerDealerId);

        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<FinalBid> bidsPage = finalBidRepo.findByBuyerDealerId(buyerDealerId, pageable);

        if (bidsPage != null && !bidsPage.isEmpty()) {
            logger.debug("Found {} bids for buyerDealerId: {}", bidsPage.getTotalElements(), buyerDealerId);

            List<FinalBidDto> finalBidDtos = bidsPage.stream().map(bid -> {
                FinalBidDto dto = new FinalBidDto();
                dto.setFinalBidId(bid.getFinalBidId());
                dto.setSellerDealerId(bid.getSellerDealerId());
                dto.setBuyerDealerId(bid.getBuyerDealerId());
                dto.setBidCarId(bid.getBidCarId());
                dto.setPrice(bid.getPrice());
                Integer beadingCarId = bidCarsRepo.findBeadingCarIdByBidCarId(bid.getBidCarId());
                dto.setBeadingCarId(beadingCarId);
                return dto;
            }).collect(Collectors.toList());

            return new PageImpl<>(finalBidDtos, pageable, bidsPage.getTotalElements());
        } else {
            logger.error("No bids found for buyerDealerId: {}", buyerDealerId);
            throw new RuntimeException("No bids found for buyerDealerId: " + buyerDealerId);
        }
    }

    @Override
    public ResponseEntity<FinalBidDto> getFinalbidById(Integer bidCarId) {
        FinalBidDto finalBidDto = finalBidRepo.findByBidCarId(bidCarId)
                .map(bid -> {

                    FinalBidDto dto = new FinalBidDto();
                    dto.setFinalBidId(bid.getFinalBidId());
                    dto.setPrice(bid.getPrice());
                    dto.setSellerDealerId(bid.getSellerDealerId());
                    dto.setBuyerDealerId(bid.getBuyerDealerId());
                    dto.setBidCarId(bid.getBidCarId());

                    System.err.println(dto);
                    return dto;
                })
                .orElseThrow(() -> new RuntimeException("No Data Found"));

        return ResponseEntity.ok(finalBidDto);
    }

//    @Override
//    public PlacedBidDTO getTopBid(Integer bidCarId) {
//        Optional<PlacedBid> topByBidCarIdOrderByAmountDesc = placedBidRepo.findTopBidByBidCarId(bidCarId);
//
//        PlacedBidDTO topByBidDTO = new PlacedBidDTO();
//        topByBidDTO.setUserId(topByBidCarIdOrderByAmountDesc.get().getUserId());
//        topByBidDTO.setAmount(topByBidCarIdOrderByAmountDesc.get().getAmount());
//        topByBidDTO.setBidCarId(topByBidCarIdOrderByAmountDesc.get().getBidCarId());
//        topByBidDTO.setPlacedBidId(topByBidDTO.getPlacedBidId());
//        topByBidDTO.setDateTime(topByBidCarIdOrderByAmountDesc.get().getDateTime());
//
//        return topByBidDTO;
//    }

    @Override
    public List<FinalBidDto> getAllFinalBids() {
        List<FinalBid> all = finalBidRepo.findAll();

        return all.stream()
                .map(finalBid -> {
                    FinalBidDto dto = new FinalBidDto();
            dto.setFinalBidId(finalBid.getFinalBidId());
            dto.setBidCarId(finalBid.getBidCarId());
            dto.setPrice(finalBid.getPrice());
            dto.setBuyerDealerId(finalBid.getBuyerDealerId());
            dto.setSellerDealerId(dto.getSellerDealerId());
            return dto;
    })
                .collect(Collectors.toList());
    }

    public PlacedBid convertToEntity(PlacedBidDTO placedBidDTO){
        PlacedBid toEntity = modelMapper.map(placedBidDTO, PlacedBid.class);
        return toEntity;
    }

    public PlacedBidDTO convertToDto(PlacedBid placedBid){
        PlacedBidDTO toDto = modelMapper.map(placedBid, PlacedBidDTO.class);
        toDto.setPlacedBidId(placedBid.getPlacedBidId());
        return toDto;
    }


}