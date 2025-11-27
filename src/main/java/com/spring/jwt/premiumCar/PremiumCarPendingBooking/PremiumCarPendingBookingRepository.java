package com.spring.jwt.premiumCar.PremiumCarPendingBooking;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PremiumCarPendingBookingRepository extends JpaRepository<PremiumCarPendingBooking, Long> {
    Optional<List<PremiumCarPendingBooking>> getAllPendingBookingByUserId(int userId);

    Optional<List<PremiumCarPendingBooking>> findByDealerId(int dealerId);

    @Query("SELECT p FROM PremiumCarPendingBooking p WHERE p.premiumCarCar.premiumCarId = :carId")
    List<PremiumCarPendingBooking> findByCarCarId(@Param("carId") int carId);


//    @Query("SELECT p FROM PremiumCarPendingBooking p WHERE p.car.carId = :carId")
//    List<PremiumCarPendingBooking> findByCarCarId(int carId);
}
