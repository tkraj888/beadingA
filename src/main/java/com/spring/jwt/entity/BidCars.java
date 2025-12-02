package com.spring.jwt.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "BidCars")
public class BidCars {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bidCarId")
    private Integer bidCarId;

    @Column(name = "beadingCarId")
    private Integer beadingCarId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "basePrice")
    private Integer basePrice;

    @Column(name = "userId")
    private Integer userId;

    @Column(name = "closingTime")
    private LocalDateTime closingTime;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            ZonedDateTime nowInIST = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
            createdAt = nowInIST.toLocalDateTime();
        }

        if (closingTime == null) {
            closingTime = createdAt.plusMinutes(30);
        }
    }
}
