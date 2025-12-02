package com.spring.jwt.premiumCar.PremiumCarFilter;

import com.spring.jwt.dto.FilterDto;
import com.spring.jwt.premiumCar.FilterDto1;
import com.spring.jwt.premiumCar.PremiumCarDto;

import java.util.List;

public interface PremiumCarFilterService {

    public List<PremiumCarDto> searchByFilter(FilterDto1 filterDto);

    List<PremiumCarDto> searchByFilterPage(FilterDto1 filterDto, int pageNo, int pageSize);

    public List<PremiumCarDto> getAllCarsWithPages(int PageNo,int pageSize);

    public List<PremiumCarDto> searchBarFilter(String searchBarInput,int PageNo);

    List<PremiumCarDto> getTop4Cars(String searchBarInput);
}



