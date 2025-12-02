package com.spring.jwt.service;

import com.spring.jwt.Interfaces.SaveCarService;
import com.spring.jwt.dto.saveCar.SaveCarDto;
import com.spring.jwt.entity.Car;
import com.spring.jwt.entity.SaveCar;
import com.spring.jwt.entity.User;
import com.spring.jwt.exception.CarNotFoundException;
import com.spring.jwt.exception.SaveCarAlreadyExistsException;
import com.spring.jwt.exception.UserNotFoundExceptions;
import com.spring.jwt.repository.CarRepo;
import com.spring.jwt.repository.SaveCarRepo;
import com.spring.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SaveCarServiceImpl implements SaveCarService {
    private final SaveCarRepo saveCarRepo;

    private final UserRepository userRepository;

    private final CarRepo carRepo;

    @Override
    public String saveCars(SaveCarDto saveCarDto) {
        Optional<User> optionalUser = userRepository.findById(saveCarDto.getUserId());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Optional<Car> optionalCar = carRepo.findById(saveCarDto.getCarId());
            if (optionalCar.isPresent()) {
                Car car = optionalCar.get();
                SaveCar saveCar = new SaveCar();
                saveCar.setCarId(saveCarDto.getCarId());
                saveCar.setUserId(saveCarDto.getUserId());
                try {
                    saveCarRepo.save(saveCar);
                    return "Car saved successfully.";
                } catch (DataIntegrityViolationException e) {
                    throw new SaveCarAlreadyExistsException("This car is already saved for this user.");
                }
            } else {
                throw new CarNotFoundException("Car with ID " + saveCarDto.getCarId() + " not found.");
            }
        } else {
            throw new UserNotFoundExceptions("User with ID " + saveCarDto.getUserId() + " not found.");
        }
    }

    @Override
    public SaveCar getSavedCarbyId(Integer saveCarId) {
        try {
            Optional<SaveCar> optionalSaveCar = saveCarRepo.findById(saveCarId);
            if (optionalSaveCar.isPresent()) {
                return optionalSaveCar.get();
            } else {
                throw new CarNotFoundException("Saved car with ID " + saveCarId + " not found.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving saved car: " + e.getMessage(), e);
        }
    }

    @Override
    public List<SaveCarDto> getSavedCar(int userId) {
        List<SaveCar> savedCars = saveCarRepo.findByUserIdOrderBySaveCarIdDesc(userId);
        if (savedCars.isEmpty()) {
            throw new UserNotFoundExceptions("No saved cars found for user with ID " + userId);
        }
        return savedCars.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private SaveCarDto convertToDto(SaveCar saveCar) {
        SaveCarDto dto = new SaveCarDto();
        dto.setSaveCarId(saveCar.getSaveCarId());
        dto.setCarId(saveCar.getCarId());
        dto.setUserId(saveCar.getUserId());
        return dto;
    }

    @Override
    public String deleteSavedCarById(Integer saveCarId) {
        try {
            Optional<SaveCar> optionalSaveCar = saveCarRepo.findById(saveCarId);
            if (optionalSaveCar.isPresent()) {
                saveCarRepo.deleteById(saveCarId);
                return "Saved car deleted successfully.";
            } else {
                throw new CarNotFoundException("Saved car with ID " + saveCarId + " not found.");
            }
        } catch (Exception e) {
            return "Error deleting saved car: " + e.getMessage();
        }
    }

    @Override
    public SaveCar getByCarAndUserId(int userId, Integer carId) {
        Optional<SaveCar> optionalSaveCar = saveCarRepo.findByUserIdAndCarId(userId, carId);
        if (optionalSaveCar.isPresent()) {
            return optionalSaveCar.get();
        } else {
            throw new CarNotFoundException("No saved car found for user ID " + userId + " and car ID " + carId);
        }
    }
}