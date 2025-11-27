package com.spring.jwt.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.spring.jwt.entity.Car;
import com.spring.jwt.entity.Status;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarDto {
    private int carId;

    private Boolean airbag;

    private Boolean ABS;

    private Boolean buttonStart;

    private Boolean sunroof;

    private Boolean childSafetyLocks;

    private Boolean acFeature;

    private Boolean musicFeature;

    private String area;

    private String brand;

    private Boolean carInsurance;

    private Status carStatus;

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

    private String safetyDescription;

    private String transmission;

    private String title;

    private String variant;

    private String carInsuranceDate;

    private int year;

    private DealerDto dealer;

    private int dealer_id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private String carInsuranceType;

    private String mainCarId;



    public CarDto(Car car){
        this.carId = car.getId();
        this.acFeature = car.getAcFeature();
        this.musicFeature = car.getMusicFeature();
        this.area = car.getArea();
        this.brand = car.getBrand();
        this.carInsurance = car.getCarInsurance();
        this.carStatus = car.getCarStatus();
        this.city = car.getCity();
        this.color = car.getColor();
        this.description = car.getDescription();
        this.fuelType =car.getFuelType();
        this.kmDriven = car.getKmDriven();
        this.model=car.getModel();
        this.ownerSerial = car.getOwnerSerial();
        this.powerWindowFeature = car.getPowerWindowFeature();
        this.price = car.getPrice();
        this.rearParkingCameraFeature = car.getRearParkingCameraFeature();
        this.registration = car.getRegistration();
        this.transmission = car.getTransmission();
        this.year = car.getYear();
        this.dealer_id=car.getDealerId();
        this.date = car.getDate();
        this.title = car.getTitle();
        this.variant = car.getVariant();
        this.carInsuranceDate = car.getCarInsuranceDate();
        this.carInsuranceType=car.getCarInsuranceType();
        this.ABS=car.getABS();
        this.airbag=car.getAirbag();
        this.buttonStart=car.getButtonStart();
        this.sunroof=car.getSunroof();
        this.childSafetyLocks=car.getChildSafetyLocks();
        this.mainCarId=car.getMainCarId();

    }
}
