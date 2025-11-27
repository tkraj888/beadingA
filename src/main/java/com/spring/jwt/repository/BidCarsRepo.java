package com.spring.jwt.repository;

import com.spring.jwt.entity.BeadingCAR;
import com.spring.jwt.entity.BidCars;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BidCarsRepo extends JpaRepository<BidCars, Integer> {

    @Query("SELECT b FROM BidCars b WHERE b.createdAt <= :currentTime AND b.closingTime >= :currentTime")
    List<BidCars> findAllLiveCars(LocalDateTime currentTime);

    Optional<BidCars> findByBeadingCarId(Integer beadingCarId);

    @Query("SELECT b FROM BidCars b WHERE b.closingTime < :currentTime")
    List<BidCars> findByClosingTimeBefore(@Param("currentTime") LocalDateTime currentTime);

    @Query("SELECT b.beadingCarId FROM BidCars b WHERE b.bidCarId = :bidCarId")
    Integer findBeadingCarIdByBidCarId(@Param("bidCarId") Integer bidCarId);


}
