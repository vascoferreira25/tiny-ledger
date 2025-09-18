package com.example.tinyledger.ledger.transaction;

import com.example.tinyledger.ledger.account.Account;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class TransactionMapRepository implements TransactionRepository {
    private final Map<Long, Transaction> transactions = new ConcurrentHashMap<>();
    private final AtomicLong lastId = new AtomicLong();

    @Override
    public Transaction save(Transaction transaction) {
        if (transaction.getId() == null) {
            Long id = lastId.incrementAndGet();
            transaction.setId(id);
        }

        transactions.put(transaction.getId(), transaction);
        return transactions.get(transaction.getId());
    }

    @Override
    public List<Transaction> findAllTransactionsByAccount(Account account) {
        return transactions.values().stream()
                .filter(t -> t.getAccount().equals(account))
                .toList();
    }
}
