package com.spring.jwt.dto;

import lombok.Data;

import java.util.List;
@Data
public class ResponseAllFinalBidDto { private String message;
    private List<FinalBidDto> placedBids;
    private String exception;

    public ResponseAllFinalBidDto(String message, List<FinalBidDto> placedBids, String exception) {
        this.message = message;
        this.placedBids = placedBids;
        this.exception = exception;
    }
}
