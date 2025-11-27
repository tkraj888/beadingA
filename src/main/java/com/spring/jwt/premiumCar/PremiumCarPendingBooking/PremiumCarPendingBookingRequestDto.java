package com.spring.jwt.premiumCar.PremiumCarPendingBooking;

import com.spring.jwt.dto.BookingDtos.DealerDetails;
import com.spring.jwt.entity.Status;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PremiumCarPendingBookingRequestDto {

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


}
