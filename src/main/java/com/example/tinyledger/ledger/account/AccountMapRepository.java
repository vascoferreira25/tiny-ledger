package com.example.tinyledger.ledger.account;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class AccountMapRepository implements AccountRepository {
    private final Map<Long, Account> accounts = new ConcurrentHashMap<>();
    private final AtomicLong lastId = new AtomicLong();

    @Override
    public Account save(Account account) {
        if (account.getId() == null) {
            Long id = lastId.incrementAndGet();
            account.setId(id);
        }

        accounts.put(account.getId(), account);
        return accounts.get(account.getId());
    }

    @Override
    public Optional<Account> findAccountById(Long id) {
        if (!accounts.containsKey(id)) {
            return Optional.empty();
        }
        return Optional.of(accounts.get(id));
    }
}
