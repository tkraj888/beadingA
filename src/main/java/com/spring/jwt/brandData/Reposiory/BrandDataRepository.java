package com.spring.jwt.brandData.Reposiory;

import com.spring.jwt.brandData.Entity.BrandData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandDataRepository extends JpaRepository<BrandData, Integer> {

    List<BrandData> findByBrand(String brand);
    List<BrandData> findByBrandAndVariant(String brand, String variant);
    @Query("SELECT DISTINCT b.brand FROM BrandData b")
    List<String> findDistinctBrands();

    Optional<BrandData> findByBrandAndVariantAndSubVariant(String brand, String variant, String subVariant);

}
