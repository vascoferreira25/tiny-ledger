package com.example.tinyledger.ledger.transaction;

import com.example.tinyledger.ledger.account.Account;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository {
    Transaction save(Transaction transaction);

    List<Transaction> findAllTransactionsByAccount(Account account);
}
