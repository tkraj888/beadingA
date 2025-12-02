package com.spring.jwt.controller;

import com.spring.jwt.Interfaces.CarVerify;
import com.spring.jwt.dto.*;
import com.spring.jwt.exception.CarNotFoundException;
import com.spring.jwt.exception.UserNotFoundExceptions;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carVerify")
@RequiredArgsConstructor
public class CarVerifyController {
    public final CarVerify carVerify;
    @PostMapping("/verify")
    public ResponseEntity<?> verifyCar(@RequestBody CarVerifyDto carVerifyDto, @RequestParam Integer carId) {
        try {
            CarVerifyDto carVerifyDto1 = carVerify.verifyCar(carVerifyDto, carId);
            ResponseSingleCarDto responseBookingDto = new ResponseSingleCarDto("Success");
            responseBookingDto.setObject(carVerifyDto1);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseBookingDto);
        }catch (Exception e) {
            ResponseSingleCarDto responseSingleCarDto = new ResponseSingleCarDto("unsuccess");
            responseSingleCarDto.setException(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/getById")
    public ResponseEntity<?> getVerifiedCar (@RequestParam Integer carVerifiedId) {
        try {
            CarVerifyDto verifiedCar = carVerify.getVerifiedCar(carVerifiedId);
            ResponseSingleCarDto responseBookingDto = new ResponseSingleCarDto("Success");
            responseBookingDto.setObject(verifiedCar);
            return ResponseEntity.status(HttpStatus.OK).body(responseBookingDto);
        } catch (Exception e){
            ResponseSingleCarDto responseSingleCarDto = new ResponseSingleCarDto("unsuccess");
            responseSingleCarDto.setException(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/GetByUserId")
    public ResponseEntity<?> getByUserId (@RequestParam Integer userId) {
        try {
            List <CarVerifyDto> carByUserId = carVerify.getCarByUserId(userId);
            ResponseCarVerifyDto responseBookingDto = new ResponseCarVerifyDto("Success");
            responseBookingDto.setList(carByUserId);
            return ResponseEntity.status(HttpStatus.OK).body(responseBookingDto);
        }catch (UserNotFoundExceptions e){
            ResponseBookingDto responseBookingDto = new ResponseBookingDto("Unsuccess");
            responseBookingDto.setException("User Not Found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBookingDto);
        }
    }

    @GetMapping ("/getCarByCarId")
    public ResponseEntity<?> getByCarId(@RequestParam Integer carId) {
        try {
            List<CarVerifyDto> carVerifyDtos = carVerify.getByCarId(carId);
            ResponseCarVerifyDto responseBookingDto = new ResponseCarVerifyDto("Success");
            responseBookingDto.setList(carVerifyDtos);
            return ResponseEntity.status(HttpStatus.OK).body(responseBookingDto);
        }catch (CarNotFoundException e) {
            ResponseBookingDto responseBookingDto = new ResponseBookingDto("Unsuccess");
            responseBookingDto.setException("Car Not Found By Id "+ carId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBookingDto);
        }
    }
    }
