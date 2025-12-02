package com.spring.jwt.premiumCar;

public class PremiumCarResponseDTO {

    public String status;
    public String message;
    public Integer premiumCarId;

    public PremiumCarResponseDTO(String status, String message, Integer premiumCarId) {
        this.status=status;
        this.message=message;
        this.premiumCarId=premiumCarId;
    }

    public PremiumCarResponseDTO(String status, String message) {
        this.status=status;
        this.message=message;
    }
}
