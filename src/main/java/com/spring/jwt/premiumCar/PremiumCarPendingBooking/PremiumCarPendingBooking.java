package com.spring.jwt.premiumCar.PremiumCarPendingBooking;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spring.jwt.dto.PendingBookingDTO;
import com.spring.jwt.entity.Status;
import com.spring.jwt.premiumCar.PremiumCar;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "PremiumCarPendingBooking")
public class PremiumCarPendingBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "premiumCarPendingBookingId", nullable = false)
    private Long premiumCarPendingBookingId;

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

    @Column (name = "asking_price", nullable = false)
    private int askingPrice;

    @ManyToOne
    @JoinColumn(name = "premium_car_car_id")
    @JsonIgnore
    private PremiumCar premiumCarCar;

    public PremiumCarPendingBooking(PremiunCarPendingBookingDto premiunCarPendingBookingDto) {
        this.premiumCarPendingBookingId = premiumCarPendingBookingId;
        this.date = date;
        this.price = price;
        this.dealerId = dealerId;
        this.userId = userId;
        this.status = status;
        this.askingPrice = askingPrice;
        this.premiumCarCar = premiumCarCar;
    }
}
