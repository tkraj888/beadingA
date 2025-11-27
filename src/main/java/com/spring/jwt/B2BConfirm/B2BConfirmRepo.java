package com.spring.jwt.B2BConfirm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface B2BConfirmRepo extends JpaRepository<B2BConfirm, Integer> {

    Optional<B2BConfirm> findByBeadingCarIdOrderByB2BConfirmIdDesc(Integer beadingCarId);

    List<B2BConfirm> findByBuyerDealerIdOrderByB2BConfirmIdDesc(Integer buyerDealerId);

    List<B2BConfirm> findBySellerDealerIdOrderByB2BConfirmIdDesc(Integer sellerDealerId);

    List<B2BConfirm> findBySalesPersonIdOrderByB2BConfirmIdDesc(Integer salesPersonId);
}
