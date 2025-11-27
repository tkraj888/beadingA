package com.spring.jwt.dto;

import lombok.Data;

@Data
public class ProfilePhotoDto {
    private Integer profilePhotoId;

    private String documentLink;

    private Integer userId;
}
