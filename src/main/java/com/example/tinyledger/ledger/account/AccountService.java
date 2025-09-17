package com.example.tinyledger.ledger.account;

import com.example.tinyledger.ledger.account.dto.AccountResponse;
import com.example.tinyledger.ledger.account.dto.CreateAccountRequest;
import com.example.tinyledger.shared.TinyLedgerInvalidArgumentException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Create a new account with the info of the request.
     * @param request the new account info
     * @return the new account
     * @throws com.example.tinyledger.shared.TinyLedgerInvalidArgumentException when invalid info is submitted
     */
    public AccountResponse createAccount(CreateAccountRequest request) {
        if (request.getStartingBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new TinyLedgerInvalidArgumentException("Balance must not be negative");
        }

        Account newAccount = new Account();
        newAccount.setBalance(request.startingBalance);
        newAccount = accountRepository.save(newAccount);

        AccountResponse response = new AccountResponse();
        response.setId(newAccount.getId());
        response.setBalance(newAccount.getBalance());

        return response;
    }
}
