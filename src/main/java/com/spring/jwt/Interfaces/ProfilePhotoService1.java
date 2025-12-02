package com.spring.jwt.Interfaces;

import com.spring.jwt.dto.ProfilePhotoDto;

public interface ProfilePhotoService1 {

    String addPPhoto(ProfilePhotoDto profilePhotoDto);

    String deleteById(Integer userId);

    Object getByUserId(Integer userId);
}
