package com.spring.jwt.B2B;

import com.spring.jwt.B2B.B2B;
import com.spring.jwt.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface B2BRepo extends JpaRepository<B2B, Integer> {


    int countByRequestStatusAndBuyerDealerId(String requestStatus, int dealerId);


    List<B2B> findByRequestStatusOrderByB2BIdDesc(String requestStatus);

    List<B2B> findByRequestStatusAndBeadingCarId(String requestStatus, Integer beadingCarId);

    int countByRequestStatusAndSellerDealerId(String requestStatus, Integer sellerDealerId);

    List<B2B> findByBuyerDealerIdOrderByB2BIdDesc(Integer buyerDealerId);


    int countByBeadingCarId(Integer beadingCarId);

    List<B2B> findByBeadingCarId(Integer beadingCarId);

    List<B2B> findBySalesPersonIdOrderByB2BIdDesc(Integer salesPersonId);

    Optional<B2B> findByBeadingCarIdAndBuyerDealerId(Integer beadingCarId, Integer buyerDealerId);
}

