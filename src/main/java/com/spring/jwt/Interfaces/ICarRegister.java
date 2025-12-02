package com.spring.jwt.Interfaces;

import com.spring.jwt.dto.CarDto;
import com.spring.jwt.dto.FilterDto;
import com.spring.jwt.entity.Status;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ICarRegister {
//    public int AddCarDetails(CarDto carDto);

    public int AddCarDetails(CarDto carDto, String carType);

    public String editCarDetails(CarDto carDto,int id);

    public List<CarDto> getAllCarsWithPages(int PageNo, int pageSize);

    public Page<CarDto> getAllCarsWithCarTypeandPage(int pageNo, int pageSize, String carType);

    public String deleteCar(int carId, int DealerId);

    CarDto getCarById(int carId);

//    public Optional<List<Car>> FindByArea(String area);

    public List<CarDto> searchByFilter(FilterDto filterDto, int pageNo);

    public CarDto findById(int carId);

    public String editCarDetails(CarDto carDto);

    public List<CarDto> getDetails(int dealerId, Status carStatus, int pageNo,String carType);

    List<String> getAutocompleteSuggestions(String query);

    long getTotalCars();

    Page<CarDto> getAllCarsWithPage(int pageNo, int pageSize);

    public CarDto findByMainCarId(String mainCarId);


    public int getCarCountByStatusAndDealer(Status carStatus, int dealerId, String carType);
    public long countAllCars() ;

}
