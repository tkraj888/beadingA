package com.spring.jwt.premiumCar.PremiunCarBrandData;

import com.spring.jwt.brandData.Dto.AllBrandDataDto;
import com.spring.jwt.brandData.Dto.BrandDataDto;
import com.spring.jwt.brandData.Dto.onlyBrandDto;
import com.spring.jwt.brandData.exception.BrandNotFoundException;
import com.spring.jwt.dto.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@RestController
@RequestMapping("/premiumBrands")
public class PremiunCarBrandDataController {

    @Autowired
    PremiunCarBrandDataService premiunCarBrandDataService;

    @PostMapping("/add")
    public ResponseEntity<ResponseDto> addBrand(@RequestBody PremiunCarBrandDataDto brandDataDto) {
        try {
            PremiunCarBrandDataDto addedBrand = premiunCarBrandDataService.addBrand(brandDataDto);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("success", "Brand added successfully"));
        } catch (BrandNotFoundException brandNotFoundException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto("error", brandNotFoundException.getMessage()));
        } catch (SQLIntegrityConstraintViolationException brandExistsException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto("error", brandExistsException.getMessage()));
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<AllBrandDataDto> getAllBrands(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "50") Integer pageSize) {
        try {
            List<PremiunCarBrandDataDto> allBrands = premiunCarBrandDataService.getAllBrands(pageNo, pageSize);
            return ResponseEntity.status(HttpStatus.OK).body(new AllBrandDataDto("Brands retrieved successfully", allBrands, null));
        } catch (BrandNotFoundException brandNotFoundException) {
            AllBrandDataDto responseAllCarDto = new AllBrandDataDto("unsuccessful");
            responseAllCarDto.setException("Brand not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseAllCarDto);
        }
    }

    @PatchMapping("/edit")
    public ResponseEntity<ResponseDto> editBrand(@RequestParam Integer premiunBrandDataId, @RequestBody BrandDataDto brandDataDto) {
        try {
            String message = premiunCarBrandDataService.editBrand(premiunBrandDataId, brandDataDto);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("success", message));
        } catch (BrandNotFoundException brandNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto("error", "Brand not found"));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteBrand(@RequestParam Integer premiunBrandDataId) {
        try {
            String message = premiunCarBrandDataService.deleteBrand(premiunBrandDataId);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("success", message));
        } catch (BrandNotFoundException brandNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto("error", "Brand not found"));
        }
    }

    @GetMapping("/only-brands")
    public ResponseEntity<AllBrandDataDto> onlyBrands() {
        try {
            List<onlyBrandDto> brands = premiunCarBrandDataService.onlyBrands();
            return ResponseEntity.status(HttpStatus.OK).body(new AllBrandDataDto("Brands retrieved successfully", brands, null));
        } catch (BrandNotFoundException brandNotFoundException){
            AllBrandDataDto responseAllCarDto = new AllBrandDataDto("unsuccessful");
            responseAllCarDto.setException("Brand not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseAllCarDto);
        }
    }

    @GetMapping("/variants")
    public ResponseEntity<AllBrandDataDto> getVariants(@RequestParam String brand) {
        try {
            List<PremiunCarBrandDataDto> variants = premiunCarBrandDataService.variants(brand);
            return ResponseEntity.status(HttpStatus.OK).body(new AllBrandDataDto("Variants retrieved successfully", variants, null));
        } catch (BrandNotFoundException brandNotFoundException) {
            AllBrandDataDto responseAllCarDto = new AllBrandDataDto("unsuccessful");
            responseAllCarDto.setException("Brand not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseAllCarDto);
        }
    }

    @GetMapping("/sub-variants")
    public ResponseEntity<AllBrandDataDto> getSubVariants(@RequestParam String brand, @RequestParam String variant) {
        try {
            List<PremiunCarBrandDataDto> subVariants = premiunCarBrandDataService.subVariant(brand, variant);
            return ResponseEntity.status(HttpStatus.OK).body(new AllBrandDataDto("Sub-variants retrieved successfully", subVariants, null));
        } catch (BrandNotFoundException brandNotFoundException) {
            AllBrandDataDto responseAllCarDto = new AllBrandDataDto("unsuccessful");
            responseAllCarDto.setException("Brand not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseAllCarDto);
        }
    }

}
