package com.spring.jwt.Wallet.Interface;


import com.spring.jwt.Wallet.Dto.WalletAccountDTO;

import java.util.List;

public interface AccountService {
    List<WalletAccountDTO> getAllAccounts();
    WalletAccountDTO getAccountById(Integer accountId);
    WalletAccountDTO createAccount(WalletAccountDTO createAccountDTO);
    WalletAccountDTO updateAccount(Integer accountId, WalletAccountDTO accountDTO);
    void deleteAccount(Integer accountId);

    WalletAccountDTO getByUserId (Integer UserId);
}