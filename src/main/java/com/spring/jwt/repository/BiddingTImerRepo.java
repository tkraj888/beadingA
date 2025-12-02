package com.spring.jwt.repository;

import com.spring.jwt.entity.BiddingTimerRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BiddingTImerRepo extends JpaRepository<BiddingTimerRequest, Integer> {

    boolean existsByBeadingCarId(Integer beadingCarId);

    @Query("SELECT b FROM BiddingTimerRequest b WHERE b.beadingCarId = :beadingCarId")
    BiddingTimerRequest findByBeadingCarId(Integer beadingCarId);
}
