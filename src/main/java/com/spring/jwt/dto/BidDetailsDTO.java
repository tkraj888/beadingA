package com.spring.jwt.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BidDetailsDTO {

    private Integer bidCarId;

    private Integer beadingCarId;

    private LocalDateTime Time;

    private LocalDateTime closingTime;

    private LocalDateTime createdAt;

    private Boolean airbag;

    private Boolean ABS;

    private Boolean buttonStart;

    private Boolean sunroof;

    private Boolean childSafetyLocks;

    private Boolean musicFeature;

    private String area;

    private String brand;

    private Boolean carInsurance;

    private String carStatus;

    private String city;

    private String color;

    private String description;

    private String fuelType;

    private int kmDriven;

    private String model;

    private int ownerSerial;

    private Boolean powerWindowFeature;

    private int price;

    private Boolean rearParkingCameraFeature;

    private String registration;

    private String transmission;

    private int year;

    private LocalDate date;

    private int userId;

    private String variant;

    private String title;

    private Integer dealer_id;

}
