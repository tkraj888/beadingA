package com.spring.jwt.B2B;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
public class B2BByerGetInfoDto {

    private int beadingCarId;

    private Integer buyerDealerId;

    private String requestStatus;

    private LocalDate time;
}
