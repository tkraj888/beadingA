package com.spring.jwt.dto.BeedingDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BiddingDTO {

    private Boolean biddingStatus;

    private LocalDate date;

    private int startingPrice;
}
