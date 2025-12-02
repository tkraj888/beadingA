package com.spring.jwt.dto;

import lombok.Data;

import java.util.List;
@Data
public class ResponseAllBidCarsDTO {

    private String status;

    private String message;

    private List<BidCarsDTO> bookings;

    private String exception;

    public ResponseAllBidCarsDTO(String status) {
        this.status = status;
    }
}

