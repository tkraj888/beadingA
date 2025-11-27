package com.spring.jwt.repository;

import com.spring.jwt.dto.FinalBidDto;
import com.spring.jwt.entity.PlacedBid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlacedBidRepo extends JpaRepository<PlacedBid, Integer> {

    List<PlacedBid> findByUserId(Integer userId);

    List<PlacedBid> findByBidCarId(Integer bidCarId);

    @Query("SELECT pb FROM PlacedBid pb WHERE pb.bidCarId = :bidCarId ORDER BY pb.amount DESC LIMIT 3")
    List<PlacedBid> findTop3ByBidCarIdOrderByAmountDesc(Integer bidCarId);

    @Query("SELECT pb FROM PlacedBid pb WHERE pb.bidCarId = :bidCarId ORDER BY pb.amount DESC, pb.dateTime ASC")
    List<PlacedBid> findTopBidByBidCarId(@Param("bidCarId") Integer bidCarId, Pageable pageable);

    Optional<PlacedBid> findTopByBidCarIdOrderByAmountDesc(Integer bidCarId);
}


