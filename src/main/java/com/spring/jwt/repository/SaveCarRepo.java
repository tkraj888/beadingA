package com.spring.jwt.repository;

import com.spring.jwt.entity.SaveCar;
import com.spring.jwt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SaveCarRepo extends JpaRepository<SaveCar,Integer> {

    List<SaveCar> findByUserId(int userId);

    Optional<SaveCar> findByUserIdAndCarId(int userId, Integer carId);

    List<SaveCar> findByUserIdOrderBySaveCarIdDesc(int userId);
}
