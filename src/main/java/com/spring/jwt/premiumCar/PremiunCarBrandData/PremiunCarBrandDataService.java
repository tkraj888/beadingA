package com.spring.jwt.premiumCar.PremiunCarBrandData;

import com.spring.jwt.brandData.Dto.BrandDataDto;
import com.spring.jwt.brandData.Dto.onlyBrandDto;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public interface PremiunCarBrandDataService {
    PremiunCarBrandDataDto addBrand(PremiunCarBrandDataDto brandDataDto) throws SQLIntegrityConstraintViolationException;

    List<PremiunCarBrandDataDto> getAllBrands(Integer pageNo, Integer pageSize);
    
    String editBrand(Integer premiunBrandDataId, BrandDataDto brandDataDto);

    String deleteBrand(Integer premiunBrandDataId);

    List<onlyBrandDto> onlyBrands();

    List<PremiunCarBrandDataDto> variants(String brand);

    List<PremiunCarBrandDataDto> subVariant(String brand, String variant);
}
