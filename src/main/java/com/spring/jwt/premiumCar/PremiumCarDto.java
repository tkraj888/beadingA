package com.spring.jwt.premiumCar;

import com.spring.jwt.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class PremiumCarDto{

    private Integer premiumCarId;
    private Boolean airbag;
    private Boolean ABS;
    private Boolean buttonStart;
    private Boolean sunroof;
    private Boolean childSafetyLocks;
    private Boolean acFeature;
    private Boolean musicFeature;
    private String area;
    private String variant;
    private String brand;
    private Boolean carInsurance;
    private String carInsuranceDate;
    private String carInsuranceType;
    private boolean pendingApproval;
    private String city;
    private String color;
    private String description;
    private String fuelType;
    private Integer kmDriven;
    private String model;
    private Integer ownerSerial;
    private Boolean powerWindowFeature;
    private Integer price;
    private Boolean rearParkingCameraFeature;
    private String registration;
    private String title;
    private String transmission;
    private Integer year;
    private LocalDate date;
    private Integer dealerId;
    private long carPhotoId;
    private String mainCarId;
    private Set<Long> premiumCarPendingBookingId; // Representing PendingBooking as just IDs for simplification

    public PremiumCarDto(PremiumCar car) {
        this.premiumCarId = car.getPremiumCarId();
        this.airbag = car.getAirbag();
        this.ABS = car.getABS();
        this.buttonStart = car.getButtonStart();
        this.sunroof = car.getSunroof();
        this.childSafetyLocks = car.getChildSafetyLocks();
        this.acFeature = car.getAcFeature();
        this.musicFeature = car.getMusicFeature();
        this.area = car.getArea();
        this.variant = car.getVariant();
        this.brand = car.getBrand();
        this.carInsurance = car.getCarInsurance();
        this.carInsuranceDate = car.getCarInsuranceDate();
        this.carInsuranceType = car.getCarInsuranceType();
        this.pendingApproval = car.isPendingApproval();
        this.city = car.getCity();
        this.color = car.getColor();
        this.description = car.getDescription();
        this.fuelType = car.getFuelType();
        this.kmDriven = car.getKmDriven();
        this.model = car.getModel();
        this.ownerSerial = car.getOwnerSerial();
        this.powerWindowFeature = car.getPowerWindowFeature();
        this.price = car.getPrice();
        this.rearParkingCameraFeature = car.getRearParkingCameraFeature();
        this.registration = car.getRegistration();
        this.title = car.getTitle();
        this.transmission = car.getTransmission();
        this.year = car.getYear();
        this.date = car.getDate();
        this.dealerId = car.getDealerId();
        this.carPhotoId = car.getCarPhotoId();
        this.mainCarId = car.getMainCarId();



    }


}

