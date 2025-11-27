package com.spring.jwt.controller;

import com.spring.jwt.Interfaces.BeadingCarService;
import com.spring.jwt.dto.BeadingCAR.BeadingCARDto;
import com.spring.jwt.dto.BeadingCAR.BeadingCarWithInsDto;
import com.spring.jwt.dto.BidCarsDTO;
import com.spring.jwt.dto.ResponceDto;
import com.spring.jwt.dto.ResponseDto;
import com.spring.jwt.exception.BeadingCarNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/BeadingCarController")
public class BeadingCarController {

    private final BeadingCarService beadingCarService;

    @PostMapping(value = "/carregister")
    public ResponseEntity<?> carRegistration(@RequestBody BeadingCARDto beadingCARDto) {
        try {
            String result = beadingCarService.AddBCar(beadingCARDto);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponceDto("success", result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponceDto("error", e.getMessage()));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllBeadingCars() {
        try {
            List<BeadingCarWithInsDto> beadingCars = beadingCarService.getAllBeadingCars();
            return ResponseEntity.status(HttpStatus.OK).body(beadingCars);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDto("error", "An error occurred while retrieving beading cars"));
        }
    }

    @GetMapping("getbyId/{id}")
    public ResponseEntity<?> getBeadingCarById(@PathVariable("id") Integer beadingCarId) {
        try {
            BeadingCarWithInsDto beadingCar = beadingCarService.getBCarById(beadingCarId);
            return ResponseEntity.status(HttpStatus.OK).body(beadingCar);
        } catch (BeadingCarNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto("error", "Beading car not found"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDto("error", "An error occurred while retrieving beading car"));
        }
    }

    @GetMapping("getByBidCarId/{id}")
    public ResponseEntity<?> getBeadingCarByIdAdd(@PathVariable("id") Integer bidCarId) {
        try {
            BeadingCarWithInsDto beadingCar = beadingCarService.getLiveCarById(bidCarId);
            return ResponseEntity.status(HttpStatus.OK).body(beadingCar);
        } catch (BeadingCarNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto("error", "Beading car not found"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDto("error", "An error occurred while retrieving beading car"));
        }
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<ResponseDto> deleteBeadingCar(@PathVariable("id") Integer beadingCarId) {
        try {
            String result = beadingCarService.deleteBCar(beadingCarId);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("success", result));
        } catch (BeadingCarNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto("error", "Beading car not found"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDto("error", "An error occurred while deleting beading car"));
        }
    }

    @PutMapping("edit/{id}")
    public ResponseEntity<ResponseDto> editCarDetails(@PathVariable("id") Integer beadingCarId, @RequestBody BeadingCARDto beadingCARDto) {
        try {
            String result = beadingCarService.editCarDetails(beadingCARDto, beadingCarId);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("success", result));
        } catch (BeadingCarNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto("error", "Beading car not found"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDto("error", "An error occurred while editing beading car details"));
        }
    }

    @GetMapping("/getByUserId1/{userId}")
    public ResponseEntity<?> getBeadingCarsByUserId(@PathVariable("userId") int userId) {
        try {
            List<BeadingCARDto> beadingCars = beadingCarService.getByUserId(userId);
            return ResponseEntity.status(HttpStatus.OK).body(beadingCars);
        } catch (BeadingCarNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto("error", "No Beading cars found for the user"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDto("error", "An error occurred while retrieving beading cars for the user"));
        }
    }

    @GetMapping("/getByDealerID/{dealerId}")
    public ResponseEntity<?> getBeadingCarsByDealerId(@PathVariable("dealerId") Integer dealerId) {
        try {
            List<BeadingCARDto> beadingCars = beadingCarService.getByDealerID(dealerId);
            return ResponseEntity.status(HttpStatus.OK).body(beadingCars);
        } catch (BeadingCarNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto("error", "No Beading cars found for the dealer"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDto("error", "An error occurred while retrieving beading cars for the dealer"));
        }
    }

    @GetMapping("/getByStatus/{carStatus}")
    public ResponseEntity<?> getBeadingCarsByStatus(@PathVariable("carStatus") String carStatus) {
        try {
            List<BeadingCARDto> beadingCars = beadingCarService.getByStatus(carStatus);
            return ResponseEntity.status(HttpStatus.OK).body(beadingCars);
        } catch (BeadingCarNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto("error", "No Beading cars found with the given status"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDto("error", "An error occurred while retrieving beading cars with the given status"));
        }
    }
    @GetMapping("/getCountByStatusAndDealerId")
    public ResponseEntity<?> getCountByStatusAndDealerId(@RequestParam String carStatus,@RequestParam Integer dealerId) {
        try {
            Integer beadingCars = beadingCarService.getCountByStatusAndDealerId(carStatus,dealerId);
            return ResponseEntity.status(HttpStatus.OK).body(beadingCars);
        } catch (BeadingCarNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto("error", "No Beading cars found with the given status"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDto("error", "An error occurred while retrieving beading cars with the given status"));
        }
    }

    @GetMapping("/getAllLiveBiddingCars")
    public ResponseEntity<?> getAllLiveCars() {

        try {
            List<BidCarsDTO> beadingCars = beadingCarService.getAllLiveCars();
            return ResponseEntity.status(HttpStatus.OK).body(beadingCars);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDto("error", "An error occurred while retrieving beading cars"));
        }
    }
    @GetMapping("/getByUniqueBeadingCarId")
    public ResponseEntity<?> getBeadingCarByUniqueBeadingCarId(@RequestParam("uniqueBeadingCarId") String uniqueBeadingCarId) {
        try {
            BeadingCarWithInsDto beadingCar = beadingCarService.getBCarByUniqueBeadingCarId(uniqueBeadingCarId);
            return ResponseEntity.status(HttpStatus.OK).body(beadingCar);
        } catch (BeadingCarNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto("error", "Beading car not found with id: " + uniqueBeadingCarId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDto("error", "An error occurred while retrieving beading car"));
        }
    }

   }

