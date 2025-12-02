package com.spring.jwt.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Data
public class PlacedBid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "placedBid")
    private Integer placedBidId;

    @Column(name = "userId")
    private Integer userId;

    @Column(name = "bidCarId")
    private Integer bidCarId;

    @Column (name = "dateAndTime")
    private LocalDateTime dateTime;

    @Column(name = "amount")
    private Integer amount;


}
