package com.spring.jwt.dto;

import com.spring.jwt.entity.PendingBooking;
import com.spring.jwt.entity.Status;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PendingBookingDTO {

    public static final String STATUS_PENDING = "Pending";
    public static final String STATUS_ACTIVE = "Active";
    public static final String STATUS_SOLD = "Sold";

    private Integer id;
    private LocalDate date;
    private int price;
    private int askingPrice;
    private Status status;
    private Integer carId;
    private Integer dealerId;
    private Integer userId;
    private String username;
    private String mobileNumber;


    public PendingBookingDTO() {

    }

    public PendingBookingDTO(PendingBooking pendingBooking) {
        this.date = pendingBooking.getDate();
        this.price = pendingBooking.getPrice();
        this.askingPrice = pendingBooking.getAskingPrice();
        this.status = pendingBooking.getStatus();
        this.carId = pendingBooking.getCarCar().getId();
        this.dealerId = pendingBooking.getDealerId();
        this.userId = pendingBooking.getUserId();

    }

}

