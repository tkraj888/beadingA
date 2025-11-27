package com.spring.jwt.Interfaces;

import com.spring.jwt.dto.CarDto;
import com.spring.jwt.dto.FilterDto;

import java.util.List;

public interface FilterService {
    public List<CarDto> searchByFilter(FilterDto filterDto);
    List<CarDto> searchByFilterPage(FilterDto filterDto, int pageNo, int pageSize);
    public List<CarDto> getAllCarsWithPages(int PageNo);

    public List<CarDto> searchBarFilter(String searchBarInput);

    List<CarDto> getTop4Cars();
}
