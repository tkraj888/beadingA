package com.spring.jwt.premiumCar.Do;

import com.spring.jwt.dto.ResponseDto;
import com.spring.jwt.entity.User;
import com.spring.jwt.premiumCar.PremiumCar;
import com.spring.jwt.premiumCar.PremiumCarRepository;
import com.spring.jwt.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PDocumentServiceImpl implements PremiumCarIDocument{


    private final UserRepository userRepository;

    private final PDocumentRepo documentRepo;

    @Autowired
    private final PremiumCarRepository premiumCarRepository;

    @Override
    public String addDocumentp(PDocumentDto documentDto) {
        Optional<User> userDetails = userRepository.findById(documentDto.getUserId());
        Optional<PremiumCar> carDetails = premiumCarRepository.findById(documentDto.getPremiumCarId());
        if (userDetails.isEmpty()) {
            throw new RuntimeException("User Not Found By Id ");
        }
        if (documentDto.getDocumentType().isEmpty()){
            throw new RuntimeException("Document Type is Empty.Please Provide Type of The Document!");
        }if (carDetails.isEmpty()){
            throw new RuntimeException("Car Not Found By Id");
        }

        PDocument document = new PDocument(documentDto);
        documentRepo.save(document);
        return "Document Uploaded Successfully";
    }

    @Override
    public List<PDocument> getAllDocumentp(Integer userId, String DocumentType) {
        List<PDocument> documentDetails =  documentRepo.findByDocumentTypeAndUserID(userId,DocumentType);
        if (documentDetails.isEmpty()){
            throw new RuntimeException("Document Not Found By Id");
        }
        return documentDetails;
    }

    @Override
    public List<PDocument> getByUserId(Integer userId) {
        List<PDocument> document = documentRepo.findByUserId(userId);
        if(document.isEmpty()){
            throw new RuntimeException("Document Not Found By User Id");
        }
        return document;

    }

    @Override
    public Object getByPCarID(Integer premiumCarId) {

        List<PDocument> document = documentRepo.findByPremiumCarId(premiumCarId);
        if(document.isEmpty()){
            throw new RuntimeException("Document Not Found By Car Id");
        }
        return document;
    }

    @Override
    public Object delete(Integer premiumCarId) {
        List<PDocument> documentCar = documentRepo.findByPremiumCarId(premiumCarId);
        List<Integer> documentCarIds = documentCar.stream()
                .map(PDocument::getPDocumentId)
                .collect(Collectors.toList());
        if(!documentCar.isEmpty()){
            documentRepo.deleteAllById(documentCarIds);
        }
        return new ResponseDto("success","Car Photo Deleted");
    }

    @Override
    public Object getPremiumCarIdType(Integer premiumCarId, String docType) {
        List<PDocument> documentCar = (documentRepo.findByPremiumCarId(premiumCarId))
                .stream()
                .filter(e -> e.getDocumentType().equals(docType))
                .toList();
        if (documentCar.size()<=0)throw new RuntimeException("Document Not Found By Car Id And Doctype");
        return documentCar;
    }

    @Override
    public String deleteById(Integer PDocumentId) {
        Optional<PDocument> document = documentRepo.findById(PDocumentId);
        if (document.isEmpty()){
            throw new RuntimeException("Document Not Found By Id");

        }
        documentRepo.deleteById(PDocumentId);
        return "Document Deleted By Document Id";
    }
}
