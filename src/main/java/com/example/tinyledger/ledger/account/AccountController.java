package com.example.tinyledger.ledger.account;

import com.example.tinyledger.ledger.account.dto.AccountResponse;
import com.example.tinyledger.ledger.account.dto.CreateAccountRequest;
import com.example.tinyledger.ledger.transaction.TransactionService;
import com.example.tinyledger.ledger.transaction.dto.NewTransactionRequest;
import com.example.tinyledger.ledger.transaction.dto.TransactionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {

    private final AccountService accountService;
    private final TransactionService transactionService;

    public AccountController(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@RequestBody CreateAccountRequest newAccount) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(newAccount));
    }

    @GetMapping("/{id}/balance")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    @GetMapping("/{id}/transaction")
    public ResponseEntity<List<TransactionResponse>> getAccountTransactions(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getTransactionsByAccount(id));
    }

    @PostMapping("/{id}/transaction")
    public ResponseEntity<TransactionResponse> createAccountTransaction(@PathVariable Long id, @RequestBody NewTransactionRequest newTransaction) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.createNewTransaction(id, newTransaction));
    }
}
