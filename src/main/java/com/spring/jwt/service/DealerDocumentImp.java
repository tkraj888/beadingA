package com.spring.jwt.service;

import com.spring.jwt.Interfaces.IDealerPhoto;
import com.spring.jwt.entity.Dealer;
import com.spring.jwt.entity.DocumentPhoto;
import com.spring.jwt.repository.DealerRepository;
import com.spring.jwt.repository.DocumentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DealerDocumentImp implements IDealerPhoto {
    @Autowired
    private DocumentRepo photoRepo;
    @Autowired
    private DealerRepository dealerRepository;
    @Autowired
    public DealerDocumentImp(DocumentRepo photoRepo)
    {
        this.photoRepo = photoRepo;
    }
    @Override
    public long addphoto(byte[] data){
        DocumentPhoto photo = new DocumentPhoto();
        photo.setPhoto1(data);

        return photo.getId();
    }


    @Override
    public byte[] getPhotoData(Long id) {
        // Retrieve the photo entity from the database based on the provided ID


            return null;

    }

    @Override
    public void updatePhoto(Long id, byte[] data) {
        // Retrieve the photo entity from the database based on the provided ID

            // Update the photo data

            // Save the updated photo to the database
        }

    @Override
    public void deletePhoto(Long id, int dealerId) {

    }

    @Override
    public void setDealerPhotoIdInCar(int carId, long carPhotoId) {

    }
}


