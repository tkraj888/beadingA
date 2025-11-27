package com.spring.jwt.dto.BeedingDtos;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlacedBidDTO {
    private Integer placedBidId;
    private Integer userId;
    private Integer bidCarId;
    private LocalDateTime dateTime;
    private Integer amount;

}
