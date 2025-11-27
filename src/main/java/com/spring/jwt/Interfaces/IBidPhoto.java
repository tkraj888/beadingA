package com.spring.jwt.Interfaces;

import com.spring.jwt.dto.BidCarDto;

import java.util.List;

public interface IBidPhoto {
    String addDocument(BidCarDto documentDto);

    String deleteById(Integer documentId);


    Object getById(Integer documentId);

    public String update( String doc, String doctype, String subtype, String comment,Integer bidDocumentId) ;

    public List<BidCarDto> getByDocumentType(Integer beadingCarId, String documentType);

    Object getBidCarIdType(Integer beadingCarId, String docType);

    Object getByBidCarID(Integer beadingCarId);
}
