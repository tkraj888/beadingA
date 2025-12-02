package com.spring.jwt.dto.BeadingCAR;

import com.spring.jwt.entity.BeadingCAR;
import lombok.Data;

import java.time.LocalDate;
@Data
public class BeadingCarWithInsDto {
    private Integer beadingCarId;

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

    private String carStatus;

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

    private String transmission;

    private Integer biddingTimerId;

    private Integer year;

    private LocalDate date;

    private int userId;

    private String variant;

    private String title;

    private Integer dealerId;

    private Integer inspectionReportId;

    private String carInsuranceType;

    private String biddingTimerStatus;

    private String uniqueBeadingCarId;


    public BeadingCarWithInsDto() {
    }

    public BeadingCarWithInsDto(BeadingCAR beadingCAR) {
        this.beadingCarId = beadingCAR.getBeadingCarId();
        this.acFeature = beadingCAR.getAcFeature();
        this.musicFeature = beadingCAR.getMusicFeature();
        this.carInsurance= beadingCAR.getCarInsurance();
        this.carInsuranceType=beadingCAR.getCarInsuranceType();
        this.area = beadingCAR.getArea();
        this.brand = beadingCAR.getBrand();
        this.carStatus = beadingCAR.getCarStatus();
        this.city = beadingCAR.getArea();
        this.color = beadingCAR.getColor();
        this.description = beadingCAR.getDescription();
        this.fuelType = beadingCAR.getFuelType();
        this.kmDriven = beadingCAR.getKmDriven();
        this.model = beadingCAR.getModel();
        this.ownerSerial = beadingCAR.getOwnerSerial();
        this.powerWindowFeature = beadingCAR.getPowerWindowFeature();
        this.price = beadingCAR.getPrice();
        this.rearParkingCameraFeature = beadingCAR.getRearParkingCameraFeature();
        this.registration = beadingCAR.getRegistration();
        this.transmission = beadingCAR.getTransmission();
        this.year = beadingCAR.getYear();
        this.date = beadingCAR.getDate();
        this.userId = beadingCAR.getUserId();
        this.title= beadingCAR.getTitle();
        this.variant= beadingCAR.getVariant();
        this.dealerId= beadingCAR.getDealerId();
        this.inspectionReportId=beadingCAR.getInspectionReportId();
        this.sunroof=beadingCAR.getSunroof();
        this.ABS=beadingCAR.getABS();
        this.airbag=beadingCAR.getAirbag();
        this.buttonStart=beadingCAR.getButtonStart();
        this.childSafetyLocks=beadingCAR.getChildSafetyLocks();
        this.biddingTimerStatus =biddingTimerStatus;
        this.uniqueBeadingCarId=beadingCAR.getUniqueBeadingCarId();
    }
}

