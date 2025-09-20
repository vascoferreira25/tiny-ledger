package com.example.tinyledger.ledger.account;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository {
    Account save(Account account);

    Optional<Account> findAccountById(Long id);
}
