package com.spring.jwt.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
public class FinalBidDto {
    private Integer finalBidId;

    private Integer sellerDealerId;

    private Integer buyerDealerId;

    private Integer bidCarId;

    private Integer price;

    private Integer beadingCarId;


}
