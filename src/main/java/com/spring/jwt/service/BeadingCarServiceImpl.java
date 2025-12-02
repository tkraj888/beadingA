package com.spring.jwt.service;

import com.spring.jwt.Interfaces.BeadingCarService;
import com.spring.jwt.dto.BeadingCAR.BeadingCARDto;
import com.spring.jwt.dto.BeadingCAR.BeadingCarWithInsDto;
import com.spring.jwt.dto.BidCarsDTO;
import com.spring.jwt.entity.*;
import com.spring.jwt.exception.BeadingCarNotFoundException;
import com.spring.jwt.exception.DealerNotFoundException;
import com.spring.jwt.exception.UserNotFoundExceptions;
import com.spring.jwt.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BeadingCarServiceImpl implements BeadingCarService {

    private final BeadingCarRepo beadingCarRepo;

    private final DealerRepository dealerRepository;

    private final UserRepository userRepository;

    private final BiddingTImerRepo biddingTImerRepo;

    private final BidCarsRepo bidCarsRepo;

    private static final String MAIN_CAR_ID_FORMAT = "%02d%02d%05d";

    public String generateMainCarId() {
        LocalDate now = LocalDate.now();
        int year = now.getYear() % 100;
        int month = now.getMonthValue();
        long nextSequenceNumber = getNextSequenceNumber();
        return String.format(MAIN_CAR_ID_FORMAT, year, month, nextSequenceNumber);
    }

    private long getNextSequenceNumber() {
        Optional<Long> lastId = beadingCarRepo.findMaxId();
        return lastId.map(id -> id + 1).orElse(1L);
    }


    @Override
    public String AddBCar(BeadingCARDto beadingCARDto) {
        User byUserId = userRepository.findByUserId(beadingCARDto.getUserId());
        if (byUserId == null) {
            throw new UserNotFoundExceptions("User not found");
        }
        Set<Role> roles = byUserId.getRoles();
        boolean isSalesPerson = roles.stream().anyMatch(role -> "INSPECTOR".equals(role.getName()));
        if (!isSalesPerson) {
            throw new RuntimeException("You're not authorized to perform this action");
        }

        if (beadingCARDto.getDealerId() == null) {
            throw new DealerNotFoundException("Dealer ID is required for adding a BeadingCAR");
        }
        if (!userRepository.existsById(beadingCARDto.getUserId())) {
            throw new UserNotFoundExceptions("User not found with ID: " + beadingCARDto.getUserId());
        }
        if (!dealerRepository.existsById(beadingCARDto.getDealerId())) {
            throw new DealerNotFoundException("Dealer not found with ID: " + beadingCARDto.getDealerId());
        }
        BeadingCAR beadingCAR = new BeadingCAR(beadingCARDto);
        beadingCAR.setCarStatus("pending");
        String uniqueBeadingCarId = generateMainCarId();
        beadingCAR.setUniqueBeadingCarId(uniqueBeadingCarId);
        beadingCAR = beadingCarRepo.save(beadingCAR);
        return String.valueOf(beadingCAR.getBeadingCarId());
    }


    @Override
    public String editCarDetails(BeadingCARDto beadingCARDto, Integer beadingCarId) {
        BeadingCAR beadingCAR = beadingCarRepo.findById(beadingCarId)
                .orElseThrow(() -> new BeadingCarNotFoundException("beadingCAR not found", HttpStatus.NOT_FOUND));

        if (beadingCARDto.getAirbag() != null) {
            beadingCAR.setAirbag(beadingCARDto.getAirbag());
        }

        if (beadingCARDto.getABS() != null) {
            beadingCAR.setABS(beadingCARDto.getABS());
        }

        if (beadingCARDto.getButtonStart() != null) {
            beadingCAR.setButtonStart(beadingCARDto.getButtonStart());
        }

        if (beadingCARDto.getSunroof() != null) {
            beadingCAR.setSunroof(beadingCARDto.getSunroof());
        }

        if (beadingCARDto.getChildSafetyLocks() != null) {
            beadingCAR.setChildSafetyLocks(beadingCARDto.getChildSafetyLocks());
        }
        if (beadingCARDto.getAcFeature() != null) {
            beadingCAR.setAcFeature(beadingCARDto.getAcFeature());
        }
        if (beadingCARDto.getMusicFeature() != null) {
            beadingCAR.setMusicFeature(beadingCARDto.getMusicFeature());
        }
        if (beadingCARDto.getArea() != null) {
            beadingCAR.setArea(beadingCARDto.getArea());
        }
        if (beadingCARDto.getDate() != null) {
            beadingCAR.setDate(beadingCARDto.getDate());
        }
        if (beadingCARDto.getBrand() != null) {
            beadingCAR.setBrand(beadingCARDto.getBrand());
        }
        if (beadingCARDto.getCarInsurance() != null) {
            beadingCAR.setCarInsurance(beadingCARDto.getCarInsurance());
        }
        if (beadingCARDto.getCarStatus() != null) {
            beadingCAR.setCarStatus(beadingCARDto.getCarStatus());
        }
        if (beadingCARDto.getCity() != null) {
            beadingCAR.setCity(beadingCARDto.getCity());
        }
        if (beadingCARDto.getColor() != null) {
            beadingCAR.setColor(beadingCARDto.getColor());
        }
        if (beadingCARDto.getDescription() != null) {
            beadingCAR.setDescription(beadingCARDto.getDescription());
        }
        if (beadingCARDto.getFuelType() != null) {
            beadingCAR.setFuelType(beadingCARDto.getFuelType());
        }
        if (beadingCARDto.getKmDriven() != null) {
            beadingCAR.setKmDriven(beadingCARDto.getKmDriven());
        }
        if (beadingCARDto.getModel() != null) {
            beadingCAR.setModel(beadingCARDto.getModel());
        }
        if (beadingCARDto.getOwnerSerial() != null) {
            beadingCAR.setOwnerSerial(beadingCARDto.getOwnerSerial());
        }
        if (beadingCARDto.getPowerWindowFeature() != null) {
            beadingCAR.setPowerWindowFeature(beadingCARDto.getPowerWindowFeature());
        }
        if (beadingCARDto.getPrice() != null) {
            beadingCAR.setPrice(beadingCARDto.getPrice());
        }
        if (beadingCARDto.getRearParkingCameraFeature() != null) {
            beadingCAR.setRearParkingCameraFeature(beadingCARDto.getRearParkingCameraFeature());
        }
        if (beadingCARDto.getRegistration() != null) {
            beadingCAR.setRegistration(beadingCARDto.getRegistration());
        }
        if (beadingCARDto.getTransmission() != null) {
            beadingCAR.setTransmission(beadingCARDto.getTransmission());
        }
        if (beadingCARDto.getYear() != null) {
            beadingCAR.setYear(beadingCARDto.getYear());
        }
        if (beadingCARDto.getVariant() != null) {
            beadingCAR.setVariant(beadingCARDto.getVariant());
        }
        if (beadingCARDto.getTitle() != null) {
            beadingCAR.setTitle(beadingCARDto.getTitle());
        }
        if (beadingCARDto.getDealerId() != null) {
            beadingCAR.setDealerId(beadingCARDto.getDealerId());
        }
        if (beadingCARDto.getCarInsuranceType() != null) {
            beadingCAR.setCarInsuranceType(beadingCARDto.getCarInsuranceType());
        }

        beadingCarRepo.save(beadingCAR);
        return "beadingCAR edited";
    }



    @Override
    public List<BeadingCarWithInsDto> getAllBeadingCars() {
        List<BeadingCAR> beadingCars = beadingCarRepo.findAll(Sort.by(Sort.Direction.DESC, "beadingCarId"));

        return beadingCars.stream()
                .map(car -> {
                    BeadingCarWithInsDto dto = new BeadingCarWithInsDto(car);
                    BiddingTimerRequest biddingTimer = biddingTImerRepo.findByBeadingCarId(car.getBeadingCarId());

                    if (biddingTimer != null) {
                        dto.setBiddingTimerStatus(biddingTimer.getStatus());
                        dto.setBiddingTimerId(biddingTimer.getBiddingTimerId());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }


    @Override
    public String deleteBCar(Integer beadingCarId) {
        beadingCarRepo.deleteById(beadingCarId);
        return "Beading car deleted successfully";
    }

    @Override
    public BeadingCarWithInsDto getBCarById(Integer beadingCarId) {
        BeadingCAR beadingCAR = beadingCarRepo.findById(beadingCarId)
                .orElseThrow(() -> new BeadingCarNotFoundException("Beading car not found with id: " + beadingCarId, HttpStatus.NOT_FOUND));
        BiddingTimerRequest biddingTimer = biddingTImerRepo.findByBeadingCarId(beadingCarId);

        BeadingCarWithInsDto beadingCarWithInsDto = new BeadingCarWithInsDto(beadingCAR);
        if (biddingTimer != null) {
            beadingCarWithInsDto.setBiddingTimerStatus(biddingTimer.getStatus());
        } else {
            beadingCarWithInsDto.setBiddingTimerStatus("null");
        }

        return beadingCarWithInsDto;
    }

    @Override
    public List<BeadingCARDto> getByUserId(int UserId) {

        List<BeadingCAR> beadingCars = beadingCarRepo.findByUserId(UserId);
        if (beadingCars.isEmpty()) {
            throw new BeadingCarNotFoundException("No Beading cars found for user with id: " + UserId, HttpStatus.NOT_FOUND);
        }
        List<BeadingCARDto> dtos = new ArrayList<>();
        for (BeadingCAR beadingCAR : beadingCars) {
            dtos.add(new BeadingCARDto(beadingCAR));
        }
        return dtos;
    }

    @Override
    public List<BeadingCARDto> getByDealerID(Integer getDealerId) {
        List<BeadingCAR> beadingCars = beadingCarRepo.findByDealerId(getDealerId);
        if (beadingCars.isEmpty()) {
            throw new BeadingCarNotFoundException("No Beading cars found for dealer with id: " + getDealerId, HttpStatus.NOT_FOUND);
        }
        List<BeadingCARDto> dtos = new ArrayList<>();
        for (BeadingCAR beadingCAR : beadingCars) {
            dtos.add(new BeadingCARDto(beadingCAR));
        }
        return dtos;
    }

    @Override
    public List<BeadingCARDto> getByStatus(String carStatus) {
        List<BeadingCAR> beadingCars = beadingCarRepo.findByCarStatus(carStatus);
        if (beadingCars.isEmpty()) {
            throw new BeadingCarNotFoundException("No Beading cars found with status: " + carStatus, HttpStatus.NOT_FOUND);
        }
        List<BeadingCARDto> dtos = new ArrayList<>();
        for (BeadingCAR beadingCAR : beadingCars) {
            dtos.add(new BeadingCARDto(beadingCAR));
        }
        return dtos;

    }

    @Override
    public Integer getCountByStatusAndDealerId(String carStatus, Integer dealerId) {

        return beadingCarRepo.getCountByStatusAndDealerId(carStatus, dealerId);
    }

    @Override
    public List<BidCarsDTO> getAllLiveCars() {
        LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
        List<BidCars> liveCars = bidCarsRepo.findAllLiveCars(currentTime);
        System.err.println(liveCars);
        return liveCars.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public BeadingCarWithInsDto getBCarByUniqueBeadingCarId(String uniqueBeadingCarId) {
        BeadingCAR beadingCAR = (BeadingCAR) beadingCarRepo.findByUniqueBeadingCarId(uniqueBeadingCarId)
                .orElseThrow(() -> new BeadingCarNotFoundException("Beading car not found with id: " + uniqueBeadingCarId, HttpStatus.NOT_FOUND));
        return new BeadingCarWithInsDto(beadingCAR);
    }

    @Override
    public BidCarsDTO getLiveCar(Integer bidingCarId) {
        LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
        BidCars liveCars = bidCarsRepo.findByBeadingCarId(bidingCarId).orElseThrow(()->new RuntimeException("not foud by beading car id"));
        System.err.println(liveCars);

        return convertToDto(liveCars);
    }

    @Override
    public BeadingCarWithInsDto getLiveCarById(Integer bidCarId) {
        Optional<BidCars> bidCars= bidCarsRepo.findById(bidCarId);
        BeadingCAR beadingCAR = beadingCarRepo.findById(bidCars.get().getBeadingCarId())
                .orElseThrow(() -> new BeadingCarNotFoundException("Beading car not found with id: " +  HttpStatus.NOT_FOUND));
        BiddingTimerRequest biddingTimer = biddingTImerRepo.findByBeadingCarId(bidCars.get().getBeadingCarId());

        BeadingCarWithInsDto beadingCarWithInsDto = new BeadingCarWithInsDto(beadingCAR);
        if (biddingTimer != null) {
            beadingCarWithInsDto.setBiddingTimerStatus(biddingTimer.getStatus());
        } else {
            beadingCarWithInsDto.setBiddingTimerStatus("null");
        }

        return beadingCarWithInsDto;
    }




    private BidCarsDTO convertToDto(BidCars beadingCar) {
        BidCarsDTO dto = new BidCarsDTO();
        dto.setBidCarId(beadingCar.getBidCarId());
        dto.setBeadingCarId(beadingCar.getBeadingCarId());
        dto.setCreatedAt(beadingCar.getCreatedAt());
        dto.setBasePrice(beadingCar.getBasePrice());
        dto.setUserId(beadingCar.getUserId());
        dto.setClosingTime(beadingCar.getClosingTime());
        return dto;
    }


}
