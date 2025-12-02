package com.spring.jwt.B2B;

import com.spring.jwt.entity.Status;

import java.util.List;

public interface B2BService {
   String addB2B(B2BPostDto b2BPostDto);

   List<B2BDto> getByBeadingCarId(String requestStatus,Integer beadingCarId);

   B2BDto getByB2bId(Integer b2BId);

   int getB2BCountByStatusAndDealer(String requestStatus, Integer sellerDealerId);

   List<B2BDto> getAllB2BRecords();

   void deleteB2B(Integer b2BId);

   List<B2BDto> getByStatus(String requestStatus);

   public List<B2BByerGetInfoDto> getByBuyerDealerId(Integer buyerDealerId);

   int getCountByBeadingCarId(Integer beadingCarId);

   B2B updateB2B(Integer b2BId ,B2BDto b2BDto);

   List<B2BDto> getBySealsPerson(Integer salesPersonId);

}
