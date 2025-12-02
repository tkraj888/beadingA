package com.spring.jwt.Wallet.Service;

import com.spring.jwt.Wallet.Dto.TransactionDTO;
import com.spring.jwt.Wallet.Dto.WalletAccountDTO;
import com.spring.jwt.Wallet.Entity.Transaction;
import com.spring.jwt.Wallet.Entity.WalletAccount;
import com.spring.jwt.Wallet.Interface.AccountService;
import com.spring.jwt.Wallet.Interface.TransactionService;
import com.spring.jwt.Wallet.Repo.TransactionRepository;
import com.spring.jwt.entity.User;
import com.spring.jwt.exception.InsufficientBalanceException;
import com.spring.jwt.exception.ResourceNotFoundException;
import com.spring.jwt.exception.TransactionNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountService accountService;
    private final ModelMapper modelMapper;

    public TransactionServiceImpl(TransactionRepository transactionRepository, AccountService accountService, ModelMapper modelMapper) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
        this.modelMapper = modelMapper;
    }

    @Override
    public TransactionDTO credit(Integer accountId, Double amount) {
        WalletAccountDTO accountDTO = accountService.getAccountById(accountId);
        Double newClosingBalance = accountDTO.getOpeningBalance() + amount;
        accountDTO.setOpeningBalance(newClosingBalance);
        accountService.updateAccount(accountId, accountDTO);

        Transaction transaction = new Transaction();
        transaction.setType("Credit");
        transaction.setAmount(amount);
        transaction.setClosingBalance(newClosingBalance);
        transaction.setStatus("Successful");
        transaction.setLastUpdateTime(LocalDateTime.now());
        transaction.setAccount(modelMapper.map(accountDTO, WalletAccount.class));
        transaction.setUser();

        Transaction savedTransaction = transactionRepository.save(transaction);
        return modelMapper.map(savedTransaction, TransactionDTO.class);
    }

//    @Override
//    public TransactionDTO debit(Integer accountId, Double amount) {
//        WalletAccountDTO accountDTO = accountService.getAccountById(accountId);
//        if (accountDTO.getOpeningBalance() >= amount) {
//            Double newClosingBalance = accountDTO.getOpeningBalance() - amount;
//            accountDTO.setOpeningBalance(newClosingBalance);
//            accountService.updateAccount(accountId, accountDTO);
//
//            // Create a credit transaction for the specific account
//            Integer specificAccountId = 3; // Specify the specific account ID here
//            WalletAccountDTO specificAccountDTO = accountService.getAccountById(specificAccountId);
//
//            Double specificAccountNewBalance = specificAccountDTO.getOpeningBalance() + amount;
//            specificAccountDTO.setOpeningBalance(specificAccountNewBalance);
//            accountService.updateAccount(specificAccountId, specificAccountDTO);
//
//            // Create a debit transaction
//            Transaction transaction = new Transaction();
//            transaction.setType("Debit");
//            transaction.setAmount(amount);
//            transaction.setClosingBalance(newClosingBalance);
//            transaction.setStatus("Successful");
//            transaction.setLastUpdateTime(LocalDateTime.now());
//            transaction.setAccount(modelMapper.map(accountDTO, WalletAccount.class));
//            transaction.setOtherAccountNo(specificAccountId); // Set the specific account number
//
//            // Save the debit transaction
//            Transaction savedTransaction = transactionRepository.save(transaction);
//            return modelMapper.map(savedTransaction, TransactionDTO.class);
//        } else {
//            throw new InsufficientBalanceException("Insufficient balance for the debit operation.");
//        }
//    }



    @Override
    public TransactionDTO debit(Integer accountId, Double amount) {
        WalletAccountDTO accountDTO = accountService.getAccountById(accountId);
        if (accountDTO.getOpeningBalance() >= amount) {
            Double newClosingBalance = accountDTO.getOpeningBalance() - amount;
            accountDTO.setOpeningBalance(newClosingBalance);
            accountService.updateAccount(accountId, accountDTO);

            // Specify the specific account ID here
            Integer specificAccountId = 3;

            WalletAccountDTO specificAccountDTO = accountService.getAccountById(specificAccountId);
            Double specificAccountNewBalance = specificAccountDTO.getOpeningBalance() + amount;
            specificAccountDTO.setOpeningBalance(specificAccountNewBalance);
            accountService.updateAccount(specificAccountId, specificAccountDTO);

            Transaction transaction = new Transaction();
            transaction.setType("Debit");
            transaction.setAmount(amount);
            transaction.setClosingBalance(newClosingBalance);
            transaction.setStatus("Successful");
            transaction.setLastUpdateTime(LocalDateTime.now());
            transaction.setAccount(modelMapper.map(accountDTO, WalletAccount.class));
            transaction.setOtherAccountNo(specificAccountId);

            // Save the debit transaction
            Transaction savedTransaction = transactionRepository.save(transaction);
            return modelMapper.map(savedTransaction, TransactionDTO.class);
        } else {
            throw new InsufficientBalanceException("Insufficient balance for the debit operation.");
        }
    }


    @Override
    public TransactionDTO withdraw(Integer accountId, Double amount) {
        WalletAccountDTO accountDTO = accountService.getAccountById(accountId);
        if (accountDTO.getOpeningBalance() >= amount) {
            Double newClosingBalance = accountDTO.getOpeningBalance() - amount;
            accountDTO.setOpeningBalance(newClosingBalance);
            accountService.updateAccount(accountId, accountDTO);
            Transaction transaction = new Transaction();
            transaction.setType("Withdraw");
            transaction.setAmount(amount);
            transaction.setClosingBalance(newClosingBalance);
            transaction.setStatus("Successful");
            transaction.setLastUpdateTime(LocalDateTime.now());
            transaction.setAccount(modelMapper.map(accountDTO, WalletAccount.class));
            transaction.setUser();
            Transaction savedTransaction = transactionRepository.save(transaction);
            return modelMapper.map(savedTransaction, TransactionDTO.class);
        } else {
            throw new InsufficientBalanceException("Insufficient balance for the withdraw operation.");
        }
    }

    @Override
    public List<TransactionDTO> getByAccountId(Integer accountId) {
        try {
            List<Transaction> transactions = transactionRepository.findByAccount_AccountId(accountId);
            if (transactions.isEmpty()) {
                throw new ResourceNotFoundException("No transactions found for account with ID: " + accountId);
            }
            return transactions.stream()
                    .map(transaction -> modelMapper.map(transaction, TransactionDTO.class))
                    .collect(Collectors.toList());
        } catch (ResourceNotFoundException ex) {
            throw ex;
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while retrieving transactions by account id: " + accountId, e);
        }
    }

    @Override
    public TransactionDTO getById(Integer transactionId) {
        try {
            Transaction transaction = transactionRepository.findById(transactionId)
                    .orElseThrow(() -> new TransactionNotFoundException("Transaction not found with id: " + transactionId));
            return modelMapper.map(transaction, TransactionDTO.class);
        } catch (TransactionNotFoundException ex) {
            throw ex;
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while retrieving transaction by id: " + transactionId, e);
        }
    }
}
