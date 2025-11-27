package com.spring.jwt.Interfaces;
import com.spring.jwt.dto.ResponceDto;
import com.spring.jwt.dto.saveCar.SaveCarDto;
import com.spring.jwt.entity.SaveCar;

import java.util.List;

public interface SaveCarService {
    String saveCars (SaveCarDto saveCarDto);
    public SaveCar getSavedCarbyId(Integer saveCarId);

    public List<SaveCarDto> getSavedCar(int userId);

    public String deleteSavedCarById(Integer saveCarId);

    public SaveCar getByCarAndUserId(int userId, Integer carId);


}
