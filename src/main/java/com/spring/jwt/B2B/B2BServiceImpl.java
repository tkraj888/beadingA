package com.spring.jwt.B2B;


import com.spring.jwt.entity.*;

import com.spring.jwt.exception.UserNotFoundExceptions;

import com.spring.jwt.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class B2BServiceImpl implements B2BService {

    private final B2BRepo b2BRepo;
    private final BeadingCarRepo beadingCarRepo;
    private final DealerRepository dealerRepository;

    @Override
    @Transactional
    public String addB2B(B2BPostDto b2BPostDto) {
        try {
            Optional<B2B> existingB2B = b2BRepo.findByBeadingCarIdAndBuyerDealerId(
                    b2BPostDto.getBeadingCarId(), b2BPostDto.getBuyerDealerId());

            if (existingB2B.isPresent()) {
                throw new RuntimeException(" already Interested for this car");
            }

            B2B b2B = new B2B();
            b2B.setBeadingCarId(b2BPostDto.getBeadingCarId());
            b2B.setBuyerDealerId(b2BPostDto.getBuyerDealerId());
            b2B.setTime(b2BPostDto.getTime().atStartOfDay());

            BeadingCAR beadingCar = beadingCarRepo.findById(b2BPostDto.getBeadingCarId())
                    .orElseThrow(() -> new RuntimeException("BeadingCar not found with id: " + b2BPostDto.getBeadingCarId()));

            if (!beadingCar.getCarStatus().trim().equalsIgnoreCase("ACTIVE")) {
                throw new RuntimeException("Car is not active.");
            }

            b2B.setSellerDealerId(beadingCar.getDealerId());
            b2B.setSalesPersonId(null);
            b2B.setRequestStatus("PENDING");
            b2BRepo.save(b2B);

            return "B2B transaction added successfully.";
        } catch (RuntimeException e) {
            throw new RuntimeException("Exception: " + e.getMessage());
        }
    }



    @Override
    public List<B2BDto> getByBeadingCarId(String requestStatus, Integer beadingCarId) {
        List<B2B> b2bList = b2BRepo.findByRequestStatusAndBeadingCarId(requestStatus, beadingCarId);

        if (b2bList.isEmpty()) {
            throw new RuntimeException("No B2B transactions found for BeadingCarId: " + beadingCarId + " with status: " + requestStatus);
        }

        return b2bList.stream().map(this::mapToB2BDto).collect(Collectors.toList());
    }

    @Override
    public B2BDto getByB2bId(Integer b2BId) {
        B2B b2b = b2BRepo.findById(b2BId).orElseThrow(() -> new RuntimeException("B2B not found with id: " + b2BId));
        return mapToB2BDto(b2b);
    }


    @Override
    public int getB2BCountByStatusAndDealer(String requestStatus, Integer sellerDealerId) {
        return b2BRepo.countByRequestStatusAndSellerDealerId(requestStatus, sellerDealerId);
    }

    @Override
    public int getCountByBeadingCarId(Integer beadingCarId) {
        return b2BRepo.countByBeadingCarId(beadingCarId);
    }

    @Override
    public List<B2BDto> getByStatus(String requestStatus) {
        List<B2B> b2bList = b2BRepo.findByRequestStatusOrderByB2BIdDesc(requestStatus);

        if (b2bList.isEmpty()) {
            throw new RuntimeException("No B2B records found with status: " + requestStatus);
        }
        return b2bList.stream()
                .map(this::mapToB2BDto)
                .collect(Collectors.toList());
    }


    @Override
    public List<B2BByerGetInfoDto> getByBuyerDealerId(Integer buyerDealerId) {
        List<B2B> b2bList = b2BRepo.findByBuyerDealerIdOrderByB2BIdDesc(buyerDealerId);
        if (b2bList.isEmpty()) {
            throw new RuntimeException("No B2B records found for buyerDealerId: " + buyerDealerId);
        }
        return b2bList.stream()
                .map(this::mapToB2BByerGetInfoDto)
                .collect(Collectors.toList());
    }


    @Override
    public List<B2BDto> getAllB2BRecords() {
        List<B2B> b2bList = b2BRepo.findAll();
        if (b2bList.isEmpty()) {
            throw new RuntimeException("No B2B records found.");
        }

        return b2bList.stream()
                .map(this::mapToB2BDto)
                .collect(Collectors.toList());
    }


    @Override
    public void deleteB2B(Integer b2BId) {
        B2B b2b = b2BRepo.findById(b2BId)
                .orElseThrow(() -> new RuntimeException("B2B not found with id: " + b2BId));
        b2BRepo.delete(b2b);
    }

    @Override
    @Transactional
    public B2B updateB2B(Integer b2BId, B2BDto b2BDto) {
        B2B existingB2B = b2BRepo.findById(b2BId)
                .orElseThrow(() -> new RuntimeException("B2B not found with id: " + b2BId));

        if (b2BDto.getBeadingCarId() != null) {
            existingB2B.setBeadingCarId(b2BDto.getBeadingCarId());
        }
        if (b2BDto.getBuyerDealerId() != null) {
            existingB2B.setBuyerDealerId(b2BDto.getBuyerDealerId());
        }
        if (b2BDto.getSellerDealerId() != null) {
            existingB2B.setSellerDealerId(b2BDto.getSellerDealerId());
        }
        if (b2BDto.getTime() != null) {
            existingB2B.setTime(b2BDto.getTime().atStartOfDay());
        }
        if (b2BDto.getRequestStatus() != null) {
            existingB2B.setRequestStatus(b2BDto.getRequestStatus());
        }
        if (b2BDto.getSalesPersonId() != null) {
            existingB2B.setSalesPersonId(b2BDto.getSalesPersonId());
        }
        if (b2BDto.getMessage() != null) {
            existingB2B.setMessage(b2BDto.getMessage());
        }

        b2BRepo.save(existingB2B);
        return existingB2B;
    }

    @Override
    public List<B2BDto> getBySealsPerson(Integer salesPersonId) {
        try {
            List<B2B> b2bList = b2BRepo.findBySalesPersonIdOrderByB2BIdDesc(salesPersonId);

            if (b2bList.isEmpty()) {
                throw new RuntimeException("No B2B records found for SalesPersonId: " + salesPersonId);
            }

            return b2bList.stream()
                    .map(this::mapToB2BDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving B2B records for SalesPersonId: " + salesPersonId + ". Exception: " + e.getMessage());
        }
    }



    private B2BPostDto mapToB2BPostDto(B2B b2b) {
        B2BPostDto dto = new B2BPostDto();
        dto.setBeadingCarId(b2b.getBeadingCarId());
        dto.setBuyerDealerId(b2b.getBuyerDealerId());
        dto.setTime(b2b.getTime().toLocalDate());
        return dto;
    }

    private B2BByerGetInfoDto mapToB2BByerGetInfoDto(B2B b2b){
        B2BByerGetInfoDto dto = new B2BByerGetInfoDto();
        dto.setBeadingCarId(b2b.getBeadingCarId());
        dto.setBuyerDealerId(b2b.getBuyerDealerId());
        dto.setRequestStatus(b2b.getRequestStatus());
        return dto;
    }
    private B2BDto mapToB2BDto(B2B b2b) {
        B2BDto b2bDto = new B2BDto();

        b2bDto.setB2BId(b2b.getB2BId());
        b2bDto.setBeadingCarId(b2b.getBeadingCarId());
        b2bDto.setBuyerDealerId(b2b.getBuyerDealerId());
        b2bDto.setSellerDealerId(b2b.getSellerDealerId());
        b2bDto.setTime(LocalDate.from(b2b.getTime()));
        b2bDto.setMessage(b2b.getMessage());
        b2bDto.setRequestStatus(b2b.getRequestStatus());
        b2bDto.setSalesPersonId(b2b.getSalesPersonId());

        return b2bDto;
    }

}
