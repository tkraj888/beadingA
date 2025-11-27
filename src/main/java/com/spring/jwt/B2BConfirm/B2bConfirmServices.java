package com.spring.jwt.B2BConfirm;


import java.util.List;

public interface B2bConfirmServices {

    String addConfirmBooking (B2bConfirmPostDto b2bConfirmPostDto);

    B2BConfirmDto getByBeadingCarId(Integer beadingCarId);

    List<B2BConfirmDto> getByBuyerDealerId (Integer buyerDealerId);

    List<B2BConfirmDto> getBySellerDealerId(Integer sellerDealerId);

    List<B2BConfirmDto> getBySalesPersonId(Integer salesPersonId);

    String cancelConfirmBooking(Integer b2BConfirmId);

}
