package com.spring.jwt.service;

import com.spring.jwt.Interfaces.CarVerify;
import com.spring.jwt.dto.CarVerifyDto;
import com.spring.jwt.entity.Car;
import com.spring.jwt.entity.CarVerified;
import com.spring.jwt.entity.User;
import com.spring.jwt.exception.CarNotFoundException;
import com.spring.jwt.exception.UserNotFoundExceptions;
import com.spring.jwt.repository.CarRepo;
import com.spring.jwt.repository.CarVerifyRepo;
import com.spring.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VerifyCarServiceImpl implements CarVerify {

    public final CarRepo carRepo;

    public final CarVerifyRepo carVerifyRepo;

    public final UserRepository userRepository;
    @Override
    public CarVerifyDto verifyCar(CarVerifyDto carVerifyDto, Integer carId) {
        Optional<User> userOptional = userRepository.findById(carVerifyDto.getUserId());
        if(userOptional.isEmpty()){
            throw new UserNotFoundExceptions("User Not Found");
        }
        Optional<Car> car = carRepo.findById(carId);
        if (car.isPresent()) {
            CarVerified carVerified = new CarVerified(carVerifyDto);
            CarVerified save = carVerifyRepo.save(carVerified);
            CarVerifyDto carVerifyDto1 = new CarVerifyDto(save);
            return carVerifyDto1;


        }else {
            throw new CarNotFoundException("Car Not Found");
        }
    }

    @Override
    public CarVerifyDto getVerifiedCar(Integer carVerifiedId) {
        Optional<CarVerified> optional = carVerifyRepo.findById(carVerifiedId);
        if (optional.isPresent()){
            CarVerified verifyDto = optional.get();
            CarVerifyDto carverify = new CarVerifyDto(verifyDto);
            return carverify;

        }else {
            throw new RuntimeException("No Such CarFound");
        }
    }

    @Override
    public List<CarVerifyDto> getCarByUserId(Integer userId) {
        List<CarVerified> verify = carVerifyRepo.findByUserId(userId);
        if (verify.isEmpty()){
            throw new UserNotFoundExceptions("User Not Found");
        }else {
            return verify.stream()
                    .map(CarVerifyDto::new)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<CarVerifyDto> getByCarId(Integer carId) {
        List<CarVerified> cars = carVerifyRepo.findByCarId(carId);
        if (cars.isEmpty()){
            throw new CarNotFoundException("Car Not Found with Id "+ carId);
        }else {
            return cars.stream()
                    .map(CarVerifyDto::new)
                    .collect(Collectors.toList());
        }
    }

}
