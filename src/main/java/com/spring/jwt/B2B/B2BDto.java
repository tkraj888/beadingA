package com.spring.jwt.B2B;

import com.spring.jwt.B2B.B2B;
import com.spring.jwt.entity.Status;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class B2BDto {

    private Integer B2BId;

    private Integer beadingCarId;

    private Integer buyerDealerId;

    private Integer sellerDealerId;

    private LocalDate time;

    private String message;

    private String requestStatus;

    private Integer salesPersonId;


//    public B2BDto(Integer b2BId, Integer beadingCarId, Integer buyerDealerId, Integer sellerDealerId, LocalDateTime time, Status requestStatus) {
//    }

    public B2BDto(Integer b2BId, Integer beadingCarId, Integer buyerDealerId, Integer sellerDealerId, LocalDateTime time, String requestStatus) {
        this.B2BId = b2BId;
        this.beadingCarId = beadingCarId;
        this.buyerDealerId = buyerDealerId;
        this.sellerDealerId = sellerDealerId;
        this.time = time.toLocalDate();
        this.requestStatus = requestStatus;
    }
}
