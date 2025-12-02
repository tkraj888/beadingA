package com.spring.jwt.brandData.Interface;

import com.spring.jwt.brandData.Dto.BrandDataDto;
import com.spring.jwt.brandData.Dto.onlyBrandDto;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public interface BrandDataService {

    BrandDataDto addBrand(BrandDataDto brandDataDto) throws SQLIntegrityConstraintViolationException;

    public List<BrandDataDto> GetAllBrands(Integer pageNo, Integer pageSize);

    String editBrand(Integer brandDataId, BrandDataDto brandDataDto);

    public String deleteBrand(Integer brandDataId);

    List <onlyBrandDto> onlyBrands();

    List<BrandDataDto> variants(String brand);

    List<BrandDataDto> subVariant(String brand,String variant);

}
