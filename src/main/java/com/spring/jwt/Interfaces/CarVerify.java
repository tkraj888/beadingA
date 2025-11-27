package com.spring.jwt.Interfaces;

import com.spring.jwt.dto.CarVerifyDto;

import java.util.List;

public interface CarVerify {

    CarVerifyDto verifyCar(CarVerifyDto carVerifyDto, Integer carId);

    public CarVerifyDto getVerifiedCar(Integer carVerifiedId);

    public List<CarVerifyDto> getCarByUserId(Integer userId);

    public List<CarVerifyDto>getByCarId(Integer carId);
}
