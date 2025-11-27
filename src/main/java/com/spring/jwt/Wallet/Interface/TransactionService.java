package com.spring.jwt.Wallet.Interface;

import com.spring.jwt.Wallet.Dto.TransactionDTO;

import java.util.List;

public interface TransactionService {
    TransactionDTO credit(Integer accountId, Double amount);
    TransactionDTO debit(Integer accountId, Double amount);
    TransactionDTO withdraw(Integer accountId, Double amount);
    List<TransactionDTO> getByAccountId(Integer accountId);
    TransactionDTO getById(Integer transactionId);
}
