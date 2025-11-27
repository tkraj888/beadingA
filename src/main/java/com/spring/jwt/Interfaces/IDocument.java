package com.spring.jwt.Interfaces;


import com.spring.jwt.dto.DocumentDto;
import com.spring.jwt.entity.Document;

import java.util.List;

public interface IDocument {
    String addDocument(DocumentDto documentDto);

    List<Document> getAllDocument(Integer userId, String DocumentType);

    List<Document> getByUserId(Integer userId);

    Object getByCarID(Integer carId);

    Object delete(Integer carId);

    Object getCarIdType(Integer carId, String docType);

    public String deleteById(Integer documentId);
}