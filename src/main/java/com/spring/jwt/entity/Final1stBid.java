package com.spring.jwt.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Entity
@Data
@NoArgsConstructor
public class Final1stBid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "placed1stBidId")
    private Integer placed1stBidId;

    @Column(name = "placedBidId")
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
