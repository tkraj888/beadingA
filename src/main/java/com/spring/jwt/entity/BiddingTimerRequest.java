package com.spring.jwt.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class BiddingTimerRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="BiddingTimerId")
    private Integer BiddingTimerId;

    @Column(name="beadingCarId")
    private Integer beadingCarId;

    @Column(name="userId")
    private Integer userId;

    @Column(name="basePrice")
    private Integer basePrice;

    @Column(name="endTime")
    private LocalDateTime endTime;

    @Column
    private String Status;
    //    Set Status to Pending CLOSED

}
