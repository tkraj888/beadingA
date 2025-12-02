package com.spring.jwt.brandData.servicesImpl;

import com.spring.jwt.brandData.Interface.BrandDataService;
import com.spring.jwt.brandData.Dto.BrandDataDto;
import com.spring.jwt.brandData.Dto.onlyBrandDto;
import com.spring.jwt.brandData.Entity.BrandData;
import com.spring.jwt.brandData.exception.BrandNotFoundException;
import com.spring.jwt.brandData.Reposiory.BrandDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BrandDataServiceImpl implements BrandDataService {

    @Autowired
    private BrandDataRepository brandDataRepository;

    @Override
    public BrandDataDto addBrand(BrandDataDto brandDataDto) throws SQLIntegrityConstraintViolationException {
        Optional<BrandData> existingBrand = brandDataRepository.findByBrandAndVariantAndSubVariant(
                brandDataDto.getBrand(), brandDataDto.getVariant(), brandDataDto.getSubVariant());

        if (existingBrand.isPresent()) {
            throw new SQLIntegrityConstraintViolationException("Brand with the same combination of brand, variant, and sub-variant already exists");
        }

        BrandData brandData = new BrandData();
        brandData.setBrand(brandDataDto.getBrand());
        brandData.setVariant(brandDataDto.getVariant());
        brandData.setSubVariant(brandDataDto.getSubVariant());
        brandDataRepository.save(brandData);
        return brandDataDto;
    }


    @Override
    public List<BrandDataDto> GetAllBrands(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("brandDataId").descending());
        Page<BrandData> brandPage = brandDataRepository.findAll(pageable);
        return brandPage.getContent().stream().map(this::convertToDto).collect(Collectors.toList());
    }


    @Override
    public String editBrand(Integer brandDataId, BrandDataDto brandDataDto) {
        Optional<BrandData> brandDataOptional = brandDataRepository.findById(brandDataId);
        if (!brandDataOptional.isPresent()) {
            throw new BrandNotFoundException("Brand with ID " + brandDataId + " not found");
        }
        BrandData brandData = brandDataOptional.get();
        if (brandDataDto.getBrand() != null) {
            brandData.setBrand(brandDataDto.getBrand());
        }
        if (brandDataDto.getVariant() != null) {
            brandData.setVariant(brandDataDto.getVariant());
        }
        if (brandDataDto.getSubVariant() != null) {
            brandData.setSubVariant(brandDataDto.getSubVariant());
        }

        brandDataRepository.save(brandData);
        return "Brand edited successfully";
    }

    @Override
    public String deleteBrand(Integer brandDataId) {
        Optional<BrandData> brandDataOptional = brandDataRepository.findById(brandDataId);
        if (!brandDataOptional.isPresent()) {
            throw new BrandNotFoundException("Brand with ID " + brandDataId + " not found");
        }
        brandDataRepository.deleteById(brandDataId);
        return "Brand deleted successfully";
    }


    @Override
    public List<onlyBrandDto> onlyBrands() {
        List<String> brands = brandDataRepository.findDistinctBrands();
        return brands.stream()
                .map(onlyBrandDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<BrandDataDto> variants(String brand) {
        List<BrandData> variants = brandDataRepository.findByBrand(brand);
        if (variants.isEmpty()) {
            throw new BrandNotFoundException("No variants found for brand " + brand);
        }
        return variants.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<BrandDataDto> subVariant(String brand, String variant) {
        List<BrandData> subVariants = brandDataRepository.findByBrandAndVariant(brand, variant);
        if (subVariants.isEmpty()) {
            throw new BrandNotFoundException("No sub-variants found for brand " + brand + " and variant " + variant);
        }
        return subVariants.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private BrandDataDto convertToDto(BrandData brandData) {
        BrandDataDto dto = new BrandDataDto();
        dto.setBrandDataId(brandData.getBrandDataId());
        dto.setBrand(brandData.getBrand());
        dto.setVariant(brandData.getVariant());
        dto.setSubVariant(brandData.getSubVariant());
        return dto;
    }
}
