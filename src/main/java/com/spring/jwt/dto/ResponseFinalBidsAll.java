package com.spring.jwt.dto;

import lombok.Data;
import java.util.List;

@Data
public class ResponseFinalBidsAll {


        private String message;
        private List<FinalBidDto> finalBids;
        private String exception;
        private Integer totalPages;

        public ResponseFinalBidsAll(String message) {
            this.message = message;

        }
    }


