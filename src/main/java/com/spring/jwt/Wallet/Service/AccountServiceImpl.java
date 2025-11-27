package com.spring.jwt.Wallet.Service;

import com.spring.jwt.Wallet.Dto.WalletAccountDTO;

import com.spring.jwt.Wallet.Entity.WalletAccount;
import com.spring.jwt.Wallet.Interface.AccountService;
import com.spring.jwt.Wallet.Repo.AccountRepository;
import com.spring.jwt.exception.AccountAlreadyExistsException;
import com.spring.jwt.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;

    public AccountServiceImpl(AccountRepository accountRepository, ModelMapper modelMapper) {
        this.accountRepository = accountRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<WalletAccountDTO> getAllAccounts() {
        List<WalletAccount> accounts = accountRepository.findAll();
        return accounts.stream()
                .map(account -> modelMapper.map(account, WalletAccountDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public WalletAccountDTO getAccountById(Integer accountId) {
        WalletAccount account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));
        return modelMapper.map(account, WalletAccountDTO.class);
    }

    @Override
    public WalletAccountDTO createAccount(WalletAccountDTO createAccountDTO) {
        Integer userId = createAccountDTO.getUserId();
        Optional<WalletAccount> existingAccountOptional = accountRepository.findByUserId(userId);

        if (existingAccountOptional.isPresent()) {
            throw new AccountAlreadyExistsException("An account already exists for the user ID: " + userId);
        }

        createAccountDTO.setOpeningBalance(0.0);
        createAccountDTO.setStatus("Active");
        createAccountDTO.setLastUpdateTime(LocalDateTime.now());
        WalletAccount account = modelMapper.map(createAccountDTO, WalletAccount.class);
        WalletAccount savedAccount = accountRepository.save(account);

        return modelMapper.map(savedAccount, WalletAccountDTO.class);
    }

    @Override
    public WalletAccountDTO updateAccount(Integer accountId, WalletAccountDTO accountDTO) {
        WalletAccount account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));
        modelMapper.map(accountDTO, account);
        WalletAccount savedAccount = accountRepository.save(account);
        return modelMapper.map(savedAccount, WalletAccountDTO.class);
    }

    @Override
    public void deleteAccount(Integer accountId) {
        WalletAccount account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));
        accountRepository.delete(account);
    }

    @Override
    public WalletAccountDTO getByUserId(Integer userId) {
        try {
            WalletAccount account = accountRepository.findByUserId(userId)
                    .orElseThrow(() -> new AccountAlreadyExistsException("Wallet account not found with user id: " + userId));

            return modelMapper.map(account, WalletAccountDTO.class);
        } catch (AccountAlreadyExistsException ex) {
            throw ex;
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while retrieving wallet account by user id: " + userId, e);
        }
    }
}