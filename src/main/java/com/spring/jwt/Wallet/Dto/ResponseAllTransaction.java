package com.spring.jwt.Wallet.Dto;

import lombok.Data;

import java.util.List;
@Data
public class ResponseAllTransaction {
    private String message;
    private List<TransactionDTO> list;
    private String exception;

    public ResponseAllTransaction(String message){
        this.message=message;
    }
}

