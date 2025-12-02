package com.spring.jwt.service;

import com.spring.jwt.Interfaces.IDocument;
import com.spring.jwt.dto.DocumentDto;
import com.spring.jwt.dto.ResponseDto;
import com.spring.jwt.entity.Car;
import com.spring.jwt.entity.Document;
import com.spring.jwt.entity.User;
import com.spring.jwt.repository.CarRepo;
import com.spring.jwt.repository.DocumentRepo;
import com.spring.jwt.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class DocumentImp implements IDocument {
    private final UserRepository userRepository;

    private final DocumentRepo documentRepo;

    private final CarRepo carRepo;
    @Override
    public String addDocument(DocumentDto documentDto) {
        Optional<User> userDetails = userRepository.findById(documentDto.getUserId());
        Optional<Car> carDetails = carRepo.findById(documentDto.getCarId());
        if (userDetails.isEmpty()) {
            throw new RuntimeException("User Not Found By Id ");
        }
        if (documentDto.getDocumentType().isEmpty()){
            throw new RuntimeException("Document Type is Empty.Please Provide Type of The Document!");
        }if (carDetails.isEmpty()){
            throw new RuntimeException("Car Not Found By Id");
        }

        Document document = new Document(documentDto);
        documentRepo.save(document);
        return "Document Uploaded Successfully";
    }

    @Override
    public List<Document> getAllDocument(Integer userId, String DocumentType) {
        List<Document> documentDetails =  documentRepo.findByDocumentTypeAndUserID(userId,DocumentType);
        if (documentDetails.isEmpty()){
            throw new RuntimeException("Document Not Found By Id");
        }
        return documentDetails;

    }

    @Override
    public List<Document> getByUserId(Integer userId) {
        List<Document> document = documentRepo.findByUserId(userId);
        if(document.isEmpty()){
            throw new RuntimeException("Document Not Found By User Id");
        }
        return document;



    }

    @Override
    public Object delete(Integer carId) {
        List<Document> documentCar = documentRepo.findByCarId(carId);
        List<Integer> documentCarIds = documentCar.stream()
                .map(Document::getDocumentId)
                .collect(Collectors.toList());
        if(!documentCar.isEmpty()){
            documentRepo.deleteAllById(documentCarIds);
        }
        return new ResponseDto("success","Car Photo Deleted");
    }

    @Override
    public Object getCarIdType(Integer carId, String docType) {
        List<Document> documentCar = (documentRepo.findByCarId(carId))
                .stream()
                .filter(e -> e.getDocumentType().equals(docType))
                .toList();
        if (documentCar.size()<=0)throw new RuntimeException("Document Not Found By Car Id And Doctype");
        return documentCar;
    }

    @Override
    public Object getByCarID(Integer carId) {
        List<Document> document = documentRepo.findByCarId(carId);
        if(document.isEmpty()){
            throw new RuntimeException("Document Not Found By Car Id");
        }
        return document;
    }

    @Override
    public String deleteById(Integer documentId) {
        Optional<Document> document = documentRepo.findById(documentId);
        if (document.isEmpty()){
            throw new RuntimeException("Document Not Found By Id");

        }
        documentRepo.deleteById(documentId);
        return "Document Deleted By Document Id";
    }

}