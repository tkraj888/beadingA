package com.spring.jwt.dto;

import lombok.Data;

@Data
public class DocumentDto {

    private String documentType;

    private String documentLink;

    private Integer userId;
    private Integer carId;

}