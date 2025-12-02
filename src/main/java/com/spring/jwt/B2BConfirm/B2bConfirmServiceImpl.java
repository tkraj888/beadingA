package com.spring.jwt.B2BConfirm;


import com.spring.jwt.B2B.B2B;
import com.spring.jwt.B2B.B2BRepo;
import com.spring.jwt.entity.BeadingCAR;
import com.spring.jwt.entity.Status;
import com.spring.jwt.repository.BeadingCarRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class B2bConfirmServiceImpl implements B2bConfirmServices {

    @Autowired
    private final B2BRepo b2BRepo;
    @Autowired
    private final B2BConfirmRepo b2BConfirmRepo;

    private final BeadingCarRepo beadingCarRepo;

    private final PendingB2BRepo pendingB2BRepo;


    @Override
    @Transactional
    public String addConfirmBooking(B2bConfirmPostDto b2bConfirmPostDto) {
        try {
            B2B b2b = b2BRepo.findById(b2bConfirmPostDto.getB2BId())
                    .orElseThrow(() -> new RuntimeException("B2B not found with id: " + b2bConfirmPostDto.getB2BId()));

            B2BConfirm b2BConfirm = new B2BConfirm();
            b2BConfirm.setB2BId(b2b.getB2BId());
            b2BConfirm.setBeadingCarId(b2b.getBeadingCarId());
            b2BConfirm.setBuyerDealerId(b2b.getBuyerDealerId());
            b2BConfirm.setSellerDealerId(b2b.getSellerDealerId());
            b2BConfirm.setTime(b2b.getTime());
            b2BConfirm.setMessage(b2b.getMessage());
            b2BConfirm.setRequestStatus("Confirm");
            b2BConfirm.setSalesPersonId(b2b.getSalesPersonId());
            b2BConfirm.setPrice(b2bConfirmPostDto.getPrice());
            b2BConfirmRepo.save(b2BConfirm);
            b2BRepo.deleteById(b2bConfirmPostDto.getB2BId());
            BeadingCAR beadingCar = beadingCarRepo.findById(b2b.getBeadingCarId())
                    .orElseThrow(() -> new RuntimeException("BeadingCAR not found with id: " + b2b.getBeadingCarId()));
            beadingCar.setCarStatus("SOLD");
            beadingCarRepo.save(beadingCar);
            List<B2B> otherB2BRecords = b2BRepo.findByBeadingCarId(b2b.getBeadingCarId());
            for (B2B otherB2B : otherB2BRecords) {
                if (!otherB2B.getB2BId().equals(b2b.getB2BId())) {
                    PendingB2B pendingB2B = new PendingB2B();
                    pendingB2B.setB2BId(otherB2B.getB2BId());
                    pendingB2B.setBeadingCarId(otherB2B.getBeadingCarId());
                    pendingB2B.setBuyerDealerId(otherB2B.getBuyerDealerId());
                    pendingB2B.setSellerDealerId(otherB2B.getSellerDealerId());
                    pendingB2B.setTime(otherB2B.getTime());
                    pendingB2B.setMessage(otherB2B.getMessage());
                    pendingB2B.setRequestStatus(otherB2B.getRequestStatus());
                    pendingB2B.setSalesPersonId(otherB2B.getSalesPersonId());
                    pendingB2B.setCreatedTime(new Date(System.currentTimeMillis()));
                    pendingB2BRepo.save(pendingB2B);

                    b2BRepo.delete(otherB2B);
                }
            }

            return "Confirm booking added and B2B entries deleted successfully";
        } catch (RuntimeException e) {
            throw new RuntimeException("Exception: " + e.getMessage());
        }
    }


    @Override
    public B2BConfirmDto getByBeadingCarId(Integer beadingCarId) {
        B2BConfirm b2BConfirm = b2BConfirmRepo.findByBeadingCarIdOrderByB2BConfirmIdDesc(beadingCarId)
                .orElseThrow(() -> new RuntimeException("B2B Confirm not found with Beading Car ID: "));
        return mapToDto(b2BConfirm);
    }

    @Override
    public List<B2BConfirmDto> getByBuyerDealerId(Integer buyerDealerId) {
//        try {
        List<B2BConfirm> confirms = b2BConfirmRepo.findByBuyerDealerIdOrderByB2BConfirmIdDesc(buyerDealerId);
        if (confirms.isEmpty()) {
            throw new RuntimeException("No B2BConfirm records found for buyerDealerId: " + buyerDealerId);
        }
        return confirms.stream().map(this::mapToDto).collect(Collectors.toList());
//        } catch (RuntimeException e) {
//            throw new RuntimeException("Error while fetching B2BConfirm records by buyerDealerId: " + e.getMessage());
//        }
    }

    @Override
    public List<B2BConfirmDto> getBySellerDealerId(Integer sellerDealerId) {
        try {
            List<B2BConfirm> confirms = b2BConfirmRepo.findBySellerDealerIdOrderByB2BConfirmIdDesc(sellerDealerId);
            if (confirms.isEmpty()) {
                throw new RuntimeException("No B2BConfirm records found for sellerDealerId: " );
            }
            return confirms.stream().map(this::mapToDto).collect(Collectors.toList());
        } catch (RuntimeException e) {
            throw new RuntimeException("Error  " + e.getMessage());
        }
    }
    @Override
    public List<B2BConfirmDto> getBySalesPersonId(Integer salesPersonId) {
        try {
            List<B2BConfirm> confirms = b2BConfirmRepo.findBySalesPersonIdOrderByB2BConfirmIdDesc(salesPersonId);
            if (confirms.isEmpty()) {
                throw new RuntimeException("No B2BConfirm records found for salesPersonId: " );
            }
            return confirms.stream().map(this::mapToDto).collect(Collectors.toList());
        } catch (RuntimeException e) {
            // Adding more context to the error message
            throw new RuntimeException("Error "+ e.getMessage());
        }
    }


    @Override
    @Transactional
    public String cancelConfirmBooking(Integer b2BConfirmId) {
        try {
            Optional<B2BConfirm> confirmBookingOpt = b2BConfirmRepo.findById(b2BConfirmId);

            if (confirmBookingOpt.isPresent()) {
                B2BConfirm confirmBooking = confirmBookingOpt.get();
                Integer beadingCarId = confirmBooking.getBeadingCarId();
                b2BConfirmRepo.delete(confirmBooking);
                List<PendingB2B> pendingB2BList = pendingB2BRepo.findByBeadingCarId(beadingCarId);

                if (pendingB2BList.isEmpty()) {
                    throw new RuntimeException("No PendingB2B records found for beadingCarId: " + beadingCarId);
                }
                for (PendingB2B pendingB2B : pendingB2BList) {
                    B2B b2B = new B2B();
                    b2B.setBeadingCarId(pendingB2B.getBeadingCarId());
                    b2B.setBuyerDealerId(pendingB2B.getBuyerDealerId());
                    b2B.setSellerDealerId(pendingB2B.getSellerDealerId());
                    b2B.setTime(pendingB2B.getTime());
                    b2B.setMessage(pendingB2B.getMessage());
                    b2B.setRequestStatus(pendingB2B.getRequestStatus());
                    b2B.setSalesPersonId(pendingB2B.getSalesPersonId());
                    b2BRepo.save(b2B);
                    pendingB2BRepo.delete(pendingB2B);
                }
                BeadingCAR beadingCar = beadingCarRepo.findById(beadingCarId)
                        .orElseThrow(() -> new RuntimeException("BeadingCAR not found with id: " + beadingCarId));
                beadingCar.setCarStatus("ACTIVE");
                beadingCarRepo.save(beadingCar);
                return "Confirm booking cancelled successfully";

            } else {
                throw new RuntimeException("B2B Confirm not found with id: " + b2BConfirmId);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("Error : " + e.getMessage());
        }
    }



    // Helper method to map B2BConfirm entity to B2BConfirmDto
    private B2BConfirmDto mapToDto(B2BConfirm b2BConfirm) {
        B2BConfirmDto dto = new B2BConfirmDto();
        dto.setB2BConfirmId(b2BConfirm.getB2BConfirmId());
        dto.setB2BId(b2BConfirm.getB2BId());
        dto.setBeadingCarId(b2BConfirm.getBeadingCarId());
        dto.setBuyerDealerId(b2BConfirm.getBuyerDealerId());
        dto.setSellerDealerId(b2BConfirm.getSellerDealerId());
        dto.setTime(b2BConfirm.getTime());
        dto.setMessage(b2BConfirm.getMessage());
        dto.setRequestStatus(b2BConfirm.getRequestStatus());
        dto.setSalesPersonId(b2BConfirm.getSalesPersonId());
        dto.setPrice(b2BConfirm.getPrice());
        return dto;
    }
}
