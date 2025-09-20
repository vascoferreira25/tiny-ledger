package com.example.tinyledger.ledger.account;

import com.example.tinyledger.ledger.account.dto.AccountResponse;
import com.example.tinyledger.ledger.account.dto.CreateAccountRequest;
import com.example.tinyledger.ledger.shared.Amount;
import com.example.tinyledger.shared.TinyLedgerNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    private final Logger logger = LoggerFactory.getLogger(AccountService.class);

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Create a new account with the info of the request.
     *
     * @param request the new account info
     * @return the new account
     * @throws com.example.tinyledger.shared.TinyLedgerInvalidArgumentException when invalid info is submitted
     */
    public AccountResponse createAccount(CreateAccountRequest request) {
        Amount amount = new Amount(request.startingBalance);
        Account newAccount = new Account(amount);
        newAccount = accountRepository.save(newAccount);

        AccountResponse response = new AccountResponse();
        response.setId(newAccount.getId());
        response.setBalance(newAccount.getBalance().value());

        return response;
    }

    public AccountResponse getAccountById(Long id) {
        Account account = accountRepository.findAccountById(id).orElseThrow(() -> {
            logger.warn("Account with id {} was not found", id);
            return new TinyLedgerNotFoundException("Account not found");
        });

        AccountResponse response = new AccountResponse();
        response.setId(account.getId());
        response.setBalance(account.getBalance().value());

        return response;
    }
}
