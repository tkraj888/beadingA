package com.spring.jwt.premiumCar;

import lombok.*;

@Data
@NoArgsConstructor
@Builder
@ToString
public class FilterDto1 {
    private Integer minPrice;
    private Integer maxPrice;
    private String area;
    private String brand;
    private String model;
    private String transmission;
    private String fuelType;
    private Integer year;

    public FilterDto1(Integer minPrice, Integer maxPrice, String area, String brand, String model, String transmission, String fuelType, Integer year) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.area = area;
        this.brand = brand;
        this.model = model;
        this.transmission = transmission;
        this.fuelType = fuelType;
        this.year = year != null ? year : 0;

    }
}
