package com.spring.jwt.service;

import com.spring.jwt.Interfaces.FinalBidService;
import com.spring.jwt.dto.FinalBidDto;
import com.spring.jwt.entity.BidCars;
import com.spring.jwt.entity.Final1stBid;
import com.spring.jwt.entity.PlacedBid;
import com.spring.jwt.entity.User;
import com.spring.jwt.exception.*;
import com.spring.jwt.repository.BidCarsRepo;
import com.spring.jwt.repository.FinalBidRepo;
import com.spring.jwt.repository.PlacedBidRepo;
import com.spring.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Final1stBidServiceImpl implements FinalBidService {


    private final FinalBidRepo finalBidRepo;

    private final BidCarsRepo bidCarsRepo;

    private final ModelMapper modelMapper;

    private final UserRepository userRepository;

//    @Override
//    public String FinalPlaceBid(FinalBidDto finalBidDto) throws BidAmountLessException, BidForSelfAuctionException {
//        Integer bidCarId = finalBidDto.getBidCarId();
//        Optional<BidCars> byId = bidCarsRepo.findById(bidCarId);
//
//        if (byId.isEmpty()){
//            throw new BeadingCarNotFoundException("Bid cannot be placed as the car is not found in our database");
//        }
//        BidCars bidCar = byId.get();
//        if (finalBidDto.getUserId().equals(bidCar.getUserId())) {
//            throw new BidForSelfAuctionException("User cannot bid for their own auction.");
//        }
//        Final1stBid final1stBid = convertToEntity(finalBidDto);
//        final1stBid.setBidCarId(bidCarId);
//        final1stBid.setDateTime(LocalDateTime.now());
//
//        finalBidRepo.save(final1stBid);
//
//        return "Bid placed successfully.";
//    }


    @Override
    public String FinalPlaceBid(FinalBidDto finalBidDto) throws BidAmountLessException, BidForSelfAuctionException {
        return "";
    }

    @Override
    public List<FinalBidDto> getByUserId(Integer userId) throws UserNotFoundExceptions {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundExceptions("User not found with ID: " + userId);
        }
        List<Final1stBid> finalBidDtos = finalBidRepo.findByUserId(userId);
        return finalBidDtos.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    @Override
    public FinalBidDto getByCarID(Integer bidCarId) {
        Optional<BidCars> bidCar = bidCarsRepo.findById(bidCarId);
        if (bidCar.isEmpty()) {
            throw new BidNotFoundExceptions("Bid car not found with ID: " + bidCarId);
        }
        Final1stBid bid = finalBidRepo.findByBidCarId(bidCarId);
        if (bid == null) {
            throw new BidNotFoundExceptions("No bids found for car with ID: " + bidCarId);
        }
        return convertToDto(bid);
    }


    @Override
    public Final1stBid getByPlacedBidId(Integer placedBidId) {
        Final1stBid finalBid = finalBidRepo.findByPlacedBidId(placedBidId);
        if (finalBid == null) {
            throw new PlacedBidNotFoundExceptions("Final bid not found with placed bid ID: " + placedBidId);
        }
        return finalBid;
    }


    @Override
    public FinalBidDto getById(Integer placedBidId) {
        Optional<Final1stBid> optionalPlacedBid = finalBidRepo.findById(placedBidId);
        if (optionalPlacedBid.isPresent()) {
            Final1stBid placedBid = optionalPlacedBid.get();
            System.out.println(placedBid);
            return convertToDto(placedBid);
        } else {
            throw new PlacedBidNotFoundExceptions("PlacedBid not found with ID: " + placedBidId);
        }
    }

    @Override
    public FinalBidDto getTopOne(Integer bidCarId) {
        Optional<BidCars> bidCar = bidCarsRepo.findById(bidCarId);
        if (bidCar.isEmpty()) {
            throw new BidNotFoundExceptions("Bid car not found with ID: " + bidCarId);
        }
        Final1stBid topBid = finalBidRepo.findTop1ByBidCarIdOrderByAmountDesc(bidCarId);
        if (topBid == null) {
            throw new BidNotFoundExceptions("No bids found for car with ID: " + bidCarId);
        }

        return convertToDto(topBid);
    }


    public Final1stBid convertToEntity(FinalBidDto finalBidDto) {
        return modelMapper.map(finalBidDto, Final1stBid.class);
    }

    public FinalBidDto convertToDto(Final1stBid final1stBid) {
        return modelMapper.map(final1stBid, FinalBidDto.class);
    }
}
