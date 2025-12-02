package com.spring.jwt.premiumCar.Do;

import lombok.Data;

@Data
public class PDocumentDto {

        private String documentType;

        private String documentLink;

        private Integer userId;
        private Integer premiumCarId;

    }

