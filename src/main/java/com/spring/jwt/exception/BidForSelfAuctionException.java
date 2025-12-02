package com.spring.jwt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BidForSelfAuctionException extends Exception {
    public BidForSelfAuctionException(String s) {
    }
}

