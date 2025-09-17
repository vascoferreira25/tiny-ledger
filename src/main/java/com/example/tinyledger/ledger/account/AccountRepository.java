package com.example.tinyledger.ledger.account;

import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository {
    Account save(Account account);
}
