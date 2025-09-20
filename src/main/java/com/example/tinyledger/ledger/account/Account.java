package com.example.tinyledger.ledger.account;

import com.example.tinyledger.ledger.shared.Amount;


public class Account {
    private Long id;
    private Amount balance;

    public Account(Amount initialBalance) {
        setBalance(initialBalance);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Amount getBalance() {
        return balance;
    }

    private void setBalance(Amount balance) {
        this.balance = balance;
    }

    public void deposit(Amount amount) {
        this.balance = this.balance.add(amount);
    }

    public void withdraw(Amount amount) {
        this.balance = this.balance.subtract(amount);
    }
}
