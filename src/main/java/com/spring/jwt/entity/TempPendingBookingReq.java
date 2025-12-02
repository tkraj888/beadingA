package com.spring.jwt.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TempPendingBookingReq {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "tempPendingBookingReqid", nullable = false)
        private int tempPendingBookingReq;

        @Column(name = "date", nullable = false)
        private LocalDate date;

        @Column(name = "price", length = 45)
        private int price;

        @Column(name = "dealerId", length = 45)
        private Integer dealerId;

        @Column(name = "userId", length = 45)
        private Integer userId;

        @Enumerated(EnumType.STRING)
        private Status status;

        @Column(name = "created_date", nullable = false)
        private LocalDateTime createdDate;

        @Column (name = "asking_price", nullable = false)
        private int askingPrice;

}
