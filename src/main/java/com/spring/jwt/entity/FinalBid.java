package com.spring.jwt.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "final_bid")
public class FinalBid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "finalBidId", nullable = false)
    private Integer finalBidId;

    @Column(name = "sellerDealerId")
    private Integer sellerDealerId;

    @Column(name ="buyerDealerId")
    private Integer buyerDealerId;

    @Column(name = "bidCarId")
    private Integer bidCarId;

    @Column(name = "price")
    private Integer price;


}