package com.spring.jwt.repository;

import com.spring.jwt.entity.FinalBid;
import com.spring.jwt.entity.PlacedBid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FinalBidRepository extends JpaRepository<FinalBid, Integer> {

    boolean existsByBidCarId(Integer bidCarId);

    Page<FinalBid> findByBuyerDealerId(Integer buyerDealerId, Pageable pageable);


    Optional<FinalBid> findByBidCarId(Integer bidCarId);
}