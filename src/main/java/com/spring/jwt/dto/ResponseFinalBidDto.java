package com.spring.jwt.dto;

import lombok.Data;

import java.util.List;

@Data
public class ResponseFinalBidDto {
        private String message;
        private List<FinalBidDto> finalBids;
        private String exception;

        public ResponseFinalBidDto(String message, List<FinalBidDto> finalBids, String exception) {
            this.message = message;
            this.finalBids = finalBids;
            this.exception = exception;
        }
    }