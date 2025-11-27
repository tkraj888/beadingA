package com.spring.jwt.premiumCar.PremiunCarBrandData;

import com.spring.jwt.brandData.Dto.BrandDataDto;
import com.spring.jwt.brandData.Dto.onlyBrandDto;
import com.spring.jwt.brandData.Entity.BrandData;

import com.spring.jwt.brandData.exception.BrandNotFoundException;
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
public class PremiunCarBrandDataServiceImpl implements PremiunCarBrandDataService{

    @Autowired
    PremiunCarBrandDataRepository premiunCarBrandDataRepository;

    @Override
    public PremiunCarBrandDataDto addBrand(PremiunCarBrandDataDto brandDataDto) throws SQLIntegrityConstraintViolationException {
        Optional<PremiunCarBrandDataEntity> existingBrand = premiunCarBrandDataRepository.findByBrandAndVariantAndSubVariant(
                brandDataDto.getBrand(), brandDataDto.getVariant(), brandDataDto.getSubVariant());

        if (existingBrand.isPresent()) {
            throw new SQLIntegrityConstraintViolationException("Brand with the same combination of brand, variant, and sub-variant already exists");
        }
        PremiunCarBrandDataEntity premiunCarBrandDataEntity = PremiunCarBrandDataMapper.toEntity(brandDataDto);
        PremiunCarBrandDataEntity saveEntity = premiunCarBrandDataRepository.save(premiunCarBrandDataEntity);

        return PremiunCarBrandDataMapper.toDTO(saveEntity);

    }

    @Override
    public List<PremiunCarBrandDataDto> getAllBrands(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("premiunBrandDataId").descending());
        Page<PremiunCarBrandDataEntity> brandPage = premiunCarBrandDataRepository.findAll(pageable);
        return brandPage.getContent()
                .stream()
                .map(PremiunCarBrandDataMapper::toDTO) // using mapper's static method
                .collect(Collectors.toList());
    }

    @Override
    public String editBrand(Integer premiunBrandDataId, BrandDataDto brandDataDto) {
        Optional<PremiunCarBrandDataEntity> brandDataOptional = premiunCarBrandDataRepository.findById(premiunBrandDataId);
        if (!brandDataOptional.isPresent()) {
            throw new BrandNotFoundException("Brand with ID " + premiunBrandDataId + " not found");
        }
        PremiunCarBrandDataEntity brandData = brandDataOptional.get();
        if (brandDataDto.getBrand() != null) {
            brandData.setBrand(brandDataDto.getBrand());
        }
        if (brandDataDto.getVariant() != null) {
            brandData.setVariant(brandDataDto.getVariant());
        }
        if (brandDataDto.getSubVariant() != null) {
            brandData.setSubVariant(brandDataDto.getSubVariant());
        }

        premiunCarBrandDataRepository.save(brandData);
        return "Brand edited successfully";
    }

    @Override
    public String deleteBrand(Integer premiunBrandDataId) {
        Optional<PremiunCarBrandDataEntity> brandDataOptional = premiunCarBrandDataRepository.findById(premiunBrandDataId);
        if (!brandDataOptional.isPresent()) {
            throw new BrandNotFoundException("Brand with ID " + premiunBrandDataId + " not found");
        }
        premiunCarBrandDataRepository.deleteById(premiunBrandDataId);
        return "Brand deleted successfully";

    }

    @Override
    public List<onlyBrandDto> onlyBrands() {
        List<String> brands = premiunCarBrandDataRepository.findDistinctBrands();
        return brands.stream()
                .map(onlyBrandDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<PremiunCarBrandDataDto> variants(String brand) {
        List<PremiunCarBrandDataEntity> variantEntities = premiunCarBrandDataRepository.findByBrand(brand);

        if (variantEntities.isEmpty()) {
            throw new BrandNotFoundException("No variants found for brand " + brand);
        }

        return variantEntities.stream()
                .map(PremiunCarBrandDataMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PremiunCarBrandDataDto> subVariant(String brand, String variant) {
        List<PremiunCarBrandDataEntity> subVariants = premiunCarBrandDataRepository.findByBrandAndVariant(brand, variant);
        if (subVariants.isEmpty()) {
            throw new BrandNotFoundException("No sub-variants found for brand " + brand + " and variant " + variant);
        }
        return subVariants.stream().map(PremiunCarBrandDataMapper::toDTO).collect(Collectors.toList());
    }

}
