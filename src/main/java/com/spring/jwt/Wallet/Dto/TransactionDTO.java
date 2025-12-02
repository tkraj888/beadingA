package com.spring.jwt.Wallet.Dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionDTO {
    private Integer transactionId;
    private String type;
    private Double amount;
    private Double closingBalance;
    private String status;
    private LocalDateTime lastUpdateTime;
    private Integer accountId;
//    private WalletAccountDTO account;
}