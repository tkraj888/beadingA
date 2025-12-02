package com.spring.jwt.Wallet.Controller;
import com.spring.jwt.Wallet.Dto.ResponseAllTransaction;
import com.spring.jwt.Wallet.Dto.TransactionDTO;
import com.spring.jwt.Wallet.Interface.TransactionService;
import com.spring.jwt.dto.ResponseDto;
import com.spring.jwt.exception.InsufficientBalanceException;
import com.spring.jwt.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/credit/{accountId}")
    public ResponseEntity<ResponseDto> creditAccount(@PathVariable("accountId") Integer accountId, @RequestParam("amount") Double amount) {
        try {
            TransactionDTO transactionDTO = transactionService.credit(accountId, amount);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto("Success", "Transaction processed successfully"));
        } catch (InsufficientBalanceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto("Error", e.getMessage()));
        }
    }

    @PostMapping("/debit/{accountId}")
    public ResponseEntity<ResponseDto> debitAccount(@PathVariable("accountId") Integer accountId, @RequestParam("amount") Double amount) {
        try {
            TransactionDTO transactionDTO = transactionService.debit(accountId, amount);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto("Success", "Transaction processed successfully"));
        } catch (InsufficientBalanceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto("Error", e.getMessage()));
        }
        catch (ResourceNotFoundException resourceNotFoundException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto("Error", resourceNotFoundException.getMessage()));
        }
    }

    @PostMapping("/withdraw/{accountId}")
    public ResponseEntity<ResponseDto> withdrawFromAccount(@PathVariable("accountId") Integer accountId, @RequestParam("amount") Double amount) {
        try {
            TransactionDTO transactionDTO = transactionService.withdraw(accountId, amount);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto("Success", "Transaction processed successfully"));
        } catch (InsufficientBalanceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto("Error", e.getMessage()));
        }
        catch (ResourceNotFoundException resourceNotFoundException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto("Error", resourceNotFoundException.getMessage()));
        }
    }

    @GetMapping("/ByAccount/{accountId}")
    public ResponseEntity<ResponseAllTransaction> getTransactionsByAccountId(@PathVariable Integer accountId) {
        try {
            List<TransactionDTO> transactions = transactionService.getByAccountId(accountId);
            ResponseAllTransaction response = new ResponseAllTransaction("Transactions retrieved successfully");
            response.setList(transactions);
            return ResponseEntity.ok().body(response);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("ById/{transactionId}")
    public ResponseEntity<TransactionDTO> getTransactionById(@PathVariable Integer transactionId) {
        try {
            TransactionDTO transaction = transactionService.getById(transactionId);
            return ResponseEntity.ok().body(transaction);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}




