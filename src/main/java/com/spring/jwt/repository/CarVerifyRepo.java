package com.spring.jwt.repository;

import com.spring.jwt.entity.CarVerified;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarVerifyRepo extends JpaRepository<CarVerified, Integer> {

    List<CarVerified> findByUserId (Integer userId);

    List<CarVerified> findByCarId(Integer carId);
}
