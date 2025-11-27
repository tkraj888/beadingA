package com.spring.jwt.premiumCar.Do;

import com.spring.jwt.dto.DocumentDto;
import com.spring.jwt.entity.Document;

import java.util.List;

public interface PremiumCarIDocument {
    String addDocumentp(PDocumentDto pDocumentDto);

    List<PDocument> getAllDocumentp(Integer userId, String DocumentType);

    List<PDocument> getByUserId(Integer userId);

    Object getByPCarID(Integer premiumCarId);

    Object delete(Integer premiumCarId);

    Object getPremiumCarIdType(Integer premiumCarId, String docType);

    public String deleteById(Integer PDocumentId);
}
