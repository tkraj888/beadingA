package com.spring.jwt.controller;

import com.spring.jwt.dto.ResponceDto;
import com.spring.jwt.dto.ResponseDto;
import com.spring.jwt.dto.saveCar.ResponseAllSaveCarDto;
import com.spring.jwt.dto.saveCar.SaveCarDto;
import com.spring.jwt.entity.SaveCar;
import com.spring.jwt.exception.CarNotFoundException;
import com.spring.jwt.exception.UserNotFoundExceptions;
import com.spring.jwt.service.SaveCarServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/saveCar")
@RequiredArgsConstructor
public class SaveCarController {

    private final SaveCarServiceImpl saveCarService;
@PostMapping("/add")
    public ResponseEntity<ResponseDto> saveCar(@RequestBody SaveCarDto saveCarDto) {
        try {
            String result = saveCarService.saveCars(saveCarDto);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("success", result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDto("error", "Error: " + e.getMessage()));
        }
    }

    @GetMapping("/getBySaveCar")
    public ResponseEntity<?> getSavedCarById(@RequestParam Integer saveCarId) {
        try {
            SaveCar saveCarDto = saveCarService.getSavedCarbyId(saveCarId);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponceDto("success", saveCarDto));
        } catch (CarNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto("not available", "not found"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto("error", "not found"));
        }
    }

    @GetMapping("/GetByUser")
    public ResponseEntity<ResponseAllSaveCarDto> getSavedCars(@RequestParam int userId) {
        try {
            List<SaveCarDto> savedCars = saveCarService.getSavedCar(userId);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseAllSaveCarDto("success", savedCars, null));
        } catch (UserNotFoundExceptions e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseAllSaveCarDto("error", null, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseAllSaveCarDto("error", null, e.getMessage()));
        }
    }


    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteSavedCarById(@RequestParam Integer saveCarId) {
        try {
            String response = saveCarService.deleteSavedCarById(saveCarId);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("success", response));
        } catch (CarNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDto("error", "An error occurred: " + e.getMessage()));
        }
    }
    @GetMapping("/getByCarAndUserId")
    public ResponseEntity<?> getByCarAndUserId(@RequestParam int userId, @RequestParam Integer carId) {
        try {
            SaveCar saveCar = saveCarService.getByCarAndUserId(userId, carId);
            SaveCarDto saveCarDto = new SaveCarDto(saveCar);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponceDto("success", saveCarDto));
        } catch (CarNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto("error", "An error occurred: " + e.getMessage()));
        }
    }
}

