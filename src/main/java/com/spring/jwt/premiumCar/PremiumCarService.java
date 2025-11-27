package com.spring.jwt.premiumCar;

import com.spring.jwt.dto.FilterDto;
import com.spring.jwt.entity.Status;

import java.util.List;

public interface PremiumCarService {
    PremiumCarDto createPremiumCar(PremiumCarDto premiumCarDto);
    PremiumCarDto getPremiumCarById(int id);
    List<PremiumCarDto> getAllPremiumCars();
    PremiumCarDto updatePremiumCar(int id, PremiumCarDto premiumCarDto);
    void deletePremiumCar(int id);
    List<PremiumCarDto> getCarsByDealerPremiumCars(Integer dealerId);

    PremiumCarDto getPremiumCarByMainId(String mainCarId);

    public List<PremiumCarDto> searchByFilterPremiumCar(FilterDto filterDto, int pageNo);

    public List<PremiumCarDto> getDetails(int dealerId, Status carStatus);
    long getTotalCars();
    public int getCarCountByStatusAndDealer(Status carStatus, int dealerId);
}
