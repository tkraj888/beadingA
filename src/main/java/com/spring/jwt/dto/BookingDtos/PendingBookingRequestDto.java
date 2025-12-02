package com.spring.jwt.dto.BookingDtos;

import com.spring.jwt.entity.Car;
import com.spring.jwt.entity.Status;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PendingBookingRequestDto {
    private int carId;

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

    private int noOfWheels;

    private int ownerSerial;

    private Boolean powerWindowFeature;

    private int price;

    private Boolean rearParkingCameraFeature;

    private String registration;

    private String transmission;

    private int year;

    private String title;

    private String variant;

    private String carInsuranceDate;

    private DealerDetails dealerDetails;

    private int dealer_id;

    private LocalDate date;


    public PendingBookingRequestDto(Car car){
        this.carId = car.getId();
        this.acFeature = car.getAcFeature();
        this.musicFeature = car.getMusicFeature();
        this.date=car.getDate();
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
        this.title = car.getTitle();
        this.variant = car.getVariant();
        this.carInsuranceDate = car.getCarInsuranceDate();

    }
}
