package com.spring.jwt.dto;

import com.spring.jwt.entity.CarVerified;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@NoArgsConstructor
public class CarVerifyDto {

    private  String partName;
    private Integer carId;
    private  String PartCondition;
    private Integer userId;

    public CarVerifyDto(CarVerified carVerify) {
        this.userId=carVerify.getUserId();
        this.partName = carVerify.getPartName();
        this.carId=carVerify.getCarId();
        this.PartCondition = carVerify.getPartCondition();
    }


}
