package com.spring.jwt.dto.saveCar;

import com.spring.jwt.entity.SaveCar;
import lombok.Data;

@Data
public class SaveCarDto {

    private Integer saveCarId;

    private  Integer carId;

    private int userId;


    public SaveCarDto(SaveCar saveCar) {
        this.saveCarId = saveCar.getSaveCarId();
        this.carId = saveCar.getCarId();
        this.userId = saveCar.getUserId();
    }

    public SaveCarDto() {
    }
}
