package com.example.tinyledger.ledger.account;

import com.example.tinyledger.shared.TinyLedgerInvalidArgumentException;

import java.math.BigDecimal;


public class Account {
    private Long id;
    private BigDecimal balance;

    public Account(BigDecimal initialBalance) {
        setBalance(initialBalance);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    private void validateAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0)
            throw new TinyLedgerInvalidArgumentException("Amount must be equal or greater than zero.");
    }

    private void setBalance(BigDecimal balance) {
        validateAmount(balance);
        this.balance = balance;
    }

    public void deposit(BigDecimal amount) {
        validateAmount(amount);
        this.balance = this.balance.add(amount);
    }

    public void withdraw(BigDecimal amount) {
        validateAmount(amount);
        this.balance = this.balance.subtract(amount);
    }
}
