package com.spring.jwt.service;

import com.spring.jwt.Interfaces.ProfilePhotoService1;
import com.spring.jwt.dto.ProfilePhotoDto;
import com.spring.jwt.entity.ProfilePhoto1;
import com.spring.jwt.entity.User;
import com.spring.jwt.exception.ProfilePhotoAlreadyExistsException;
import com.spring.jwt.exception.UserNotFoundExceptions;
import com.spring.jwt.repository.ProfilePhotoRepo1;
import com.spring.jwt.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfilePhotoService1Impl implements ProfilePhotoService1 {
    @Autowired
    private ProfilePhotoRepo1 profilePhotoRepo1;
    @Autowired
    private UserRepository userRepository;
    @Transactional
    public String addPPhoto(ProfilePhotoDto profilePhotoDto) {
        Optional<ProfilePhoto1> existingProfilePhoto = profilePhotoRepo1.findByUserId(profilePhotoDto.getUserId());

        if (existingProfilePhoto.isPresent()) {
            throw new ProfilePhotoAlreadyExistsException("Profile photo for this user already exists.");
        }

        ProfilePhoto1 profilePhoto1 = new ProfilePhoto1(profilePhotoDto);
        ProfilePhoto1 savedProfilePhoto = profilePhotoRepo1.save(profilePhoto1);

//        Optional<User> userOptional = userRepository.findById(profilePhotoDto.getUserId());
//        if (userOptional.isPresent()) {
//            User user = userOptional.get();
//            user.setProfilePhotoId1(savedProfilePhoto.getProfilePhoto1Id());
//            userRepository.save(user);
//        } else {
//            throw new UserNotFoundExceptions("User not found with ID: " + profilePhotoDto.getUserId());
//        }

        return "Profile photo added";
    }

    @Override
    @Transactional
    public String deleteById(Integer userId) {
        Optional<ProfilePhoto1> profilePhoto = profilePhotoRepo1.findByUserId(userId);
        if (profilePhoto.isPresent()) {
            profilePhotoRepo1.delete(profilePhoto.get());
            return "Profile photo deleted";
        } else {
            throw new EntityNotFoundException("Profile photo not found for user ID: " + userId);
        }
    }

    @Override
    public ProfilePhotoDto getByUserId(Integer userId) {
        Optional<ProfilePhoto1> profilePhotoOptional = profilePhotoRepo1.findByUserId(userId);
        if (profilePhotoOptional.isPresent()) {
            ProfilePhoto1 profilePhoto = profilePhotoOptional.get();
            ProfilePhotoDto profilePhotoDto = new ProfilePhotoDto();
            profilePhotoDto.setProfilePhotoId(profilePhoto.getProfilePhoto1Id());
            profilePhotoDto.setDocumentLink(profilePhoto.getDocumentLink());
            profilePhotoDto.setUserId(profilePhoto.getUserId());
            return profilePhotoDto;
        } else {
            return null;
        }
    }

}
