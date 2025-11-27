package com.spring.jwt.premiumCar.PremiunCarBrandData;

import com.spring.jwt.brandData.Entity.BrandData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PremiunCarBrandDataRepository extends JpaRepository<PremiunCarBrandDataEntity,Integer> {

    Optional<PremiunCarBrandDataEntity> findByBrandAndVariantAndSubVariant(String brand, String variant, String subVariant);

    @Query("SELECT DISTINCT b.brand FROM PremiunCarBrandDataEntity b")
    List<String> findDistinctBrands();

    List<PremiunCarBrandDataEntity> findByBrand(String brand);

    List<PremiunCarBrandDataEntity> findByBrandAndVariant(String brand, String variant);
}
