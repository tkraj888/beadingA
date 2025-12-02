package com.spring.jwt.repository;

import com.spring.jwt.entity.Final1stBid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FinalBidRepo extends JpaRepository<Final1stBid, Integer> {

    List<Final1stBid> findByUserId(Integer userId);

    public Final1stBid findByBidCarId(Integer bidCarId);

    public  Final1stBid findByPlacedBidId(Integer placedBidId);

//    @Query("SELECT pb FROM PlacedBid pb WHERE pb.bidCarId = :bidCarId ORDER BY pb.amount DESC LIMIT 1")
    public Final1stBid findTop1ByBidCarIdOrderByAmountDesc(Integer bidCarId);
}
