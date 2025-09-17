package com.example.tinyledger.ledger.account;

import com.example.tinyledger.ledger.account.dto.AccountResponse;
import com.example.tinyledger.ledger.account.dto.CreateAccountRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@RequestBody CreateAccountRequest newAccount) {
        return ResponseEntity.ok(accountService.createAccount(newAccount));
    }
}
