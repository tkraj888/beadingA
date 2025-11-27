package com.spring.jwt.dto;

import java.util.List;

import com.spring.jwt.dto.BeedingDtos.PlacedBidDTO;
import lombok.Data;

@Data
public class ResponseAllPlacedBidDTO {
    private String message;
    private List<PlacedBidDTO> placedBids;
    private String exception;

    public ResponseAllPlacedBidDTO(String message, List<PlacedBidDTO> placedBids, String exception) {
        this.message = message;
        this.placedBids = placedBids;
        this.exception = exception;
    }
}
