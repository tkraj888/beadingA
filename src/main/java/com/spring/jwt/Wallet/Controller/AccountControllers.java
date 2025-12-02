package com.spring.jwt.Wallet.Controller;


import com.spring.jwt.Wallet.Dto.CreateWalletAccountDTO;
import com.spring.jwt.Wallet.Dto.WalletAccountDTO;

import com.spring.jwt.Wallet.Interface.AccountService;
import com.spring.jwt.dto.ResponseDto;
import com.spring.jwt.dto.NewResponseDto;
import com.spring.jwt.exception.AccountAlreadyExistsException;
import com.spring.jwt.exception.ResourceNotFoundException;
import com.spring.jwt.exception.UserNotFoundExceptions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("WalletAccount")
public class AccountControllers {
    private final AccountService accountService;

    public AccountControllers(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("getAllAccount")
    public ResponseEntity<?> getAllAccounts() {
        List<WalletAccountDTO> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(new NewResponseDto("Accounts retrieved successfully", accounts, null));
    }

    @GetMapping("getById/{id}")
    public ResponseEntity<?> getAccountById(@PathVariable(value = "id") Integer accountId) {
        try {
            WalletAccountDTO accountDTO = accountService.getAccountById(accountId);
            return ResponseEntity.ok(new NewResponseDto("Account retrieved successfully", accountDTO, null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new NewResponseDto("Account not found", null, e));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new NewResponseDto("Error occurred", null, ex));
        }
    }

    @PostMapping("create/wallet")
    public ResponseEntity<ResponseDto> createAccount(@RequestBody CreateWalletAccountDTO requestDTO) {
        ResponseDto response = new ResponseDto();
        try {
            WalletAccountDTO accountDTO = new WalletAccountDTO();
            accountDTO.setPanCard(requestDTO.getPanCard());
            accountDTO.setUserId(requestDTO.getUserId());
            accountDTO.setStatus(requestDTO.getStatus());
            accountDTO.setOpeningBalance(requestDTO.getOpeningBalance());

            accountDTO.setLastUpdateTime(LocalDateTime.now());

            WalletAccountDTO createdAccount = accountService.createAccount(accountDTO);
            response.setStatus("success");
            response.setMessage("Account created successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (AccountAlreadyExistsException e) {
            response.setStatus("error");
            response.setMessage("Account Already Exists by user id");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<NewResponseDto> getAccountByUserId(@PathVariable(value = "userId") Integer userId) {
        try {
            WalletAccountDTO accountDTO = accountService.getByUserId(userId);
            return ResponseEntity.ok().body(new NewResponseDto("Success", accountDTO, null));
        } catch (UserNotFoundExceptions ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new NewResponseDto("Error", null, ex));
        } catch (AccountAlreadyExistsException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new NewResponseDto("Error", null, ex));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new NewResponseDto("Error", null, e));
        }
    }

    @PutMapping("/{id}")
    public WalletAccountDTO updateAccount(@PathVariable(value = "id") Integer accountId,
                                    @RequestBody WalletAccountDTO accountDTO) {
        return accountService.updateAccount(accountId, accountDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable(value = "id") Integer accountId) {
        accountService.deleteAccount(accountId);
        return ResponseEntity.ok().build();
    }


}