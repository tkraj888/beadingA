package com.spring.jwt.premiumCar;

import com.spring.jwt.dto.ResponseDto;
import com.spring.jwt.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/premiumCars")
public class PremiumCarController {

    @Autowired
    private PremiumCarService premiumCarService;

    @PostMapping("/register")
    public ResponseEntity<PremiumCarResponseDTO> registerCar(@RequestBody PremiumCarDto premiumCarDto) {
        try {
            PremiumCarDto createdCar = premiumCarService.createPremiumCar(premiumCarDto);
            return ResponseEntity.ok(new PremiumCarResponseDTO("success", "Car registered with MainCarId: " + createdCar.getMainCarId(), createdCar.getPremiumCarId()));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new PremiumCarResponseDTO("unsuccess", ex.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new PremiumCarResponseDTO("error", "Failed to register car"));
        }
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getCarById(@PathVariable int id) {
        try {
            PremiumCarDto car = premiumCarService.getPremiumCarById(id);
            return ResponseEntity.ok(car);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDto("unsuccess", ex.getMessage()));
        }
    }

    @GetMapping("/main/{mainCarId}")
    public ResponseEntity<?> getCarByMainId(@PathVariable String mainCarId) {
        try {
            PremiumCarDto car = premiumCarService.getPremiumCarByMainId(mainCarId);
            return ResponseEntity.ok(car);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDto("unsuccess", ex.getMessage()));
        }
    }

    @GetMapping("/dealer/{dealerId}")
    public ResponseEntity<?> getCarsByDealer(@PathVariable Integer dealerId) {
        try {
            List<PremiumCarDto> cars = premiumCarService.getCarsByDealerPremiumCars(dealerId);
            return ResponseEntity.ok(cars);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDto("unsuccess", ex.getMessage()));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseDto> updateCar(@PathVariable int id, @RequestBody PremiumCarDto dto) {
        try {
            PremiumCarDto updated = premiumCarService.updatePremiumCar(id, dto);
            return ResponseEntity.ok(new ResponseDto("success", "Updated car with ID: " + updated.getPremiumCarId()));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDto("unsuccess", ex.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseDto> deleteCar(@PathVariable int id) {
        try {
            premiumCarService.deletePremiumCar(id);
            return ResponseEntity.ok(new ResponseDto("success", "Deleted car with ID: " + id));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDto("unsuccess", ex.getMessage()));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<PremiumCarDto>> getAllCars() {
        return ResponseEntity.ok(premiumCarService.getAllPremiumCars());
    }
}
