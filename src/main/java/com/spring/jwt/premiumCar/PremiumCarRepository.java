package com.spring.jwt.premiumCar;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PremiumCarRepository extends JpaRepository<PremiumCar, Integer>, JpaSpecificationExecutor<PremiumCar> {
    @Query("SELECT MAX(p.premiumCarId) FROM PremiumCar p")
    Optional<Long> findMaxId();

    List<PremiumCar> findByDealerId(Integer dealerId);

    Optional<PremiumCar> findByMainCarId(String mainCarId);

    List<PremiumCar> findTop4ByOrderByPremiumCarIdDesc();

    @Query("SELECT p FROM PremiumCar p WHERE p.carStatus = 'PENDING' OR p.carStatus = 'ACTIVATE'")
    List<PremiumCar> getPendingAndActivateCar();


    @Query("SELECT c FROM PremiumCar c WHERE " +
            "LOWER(c.area) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.brand) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.model) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.fuelType) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.transmission) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<PremiumCar> searchPremiumCarByKeyword(@Param("keyword") String keyword);

    Page<PremiumCar> findAll(Specification<PremiumCar> spec, Pageable pageable);
}
