package com.example.tinyledger.ledger.transaction;

import com.example.tinyledger.ledger.account.Account;
import com.example.tinyledger.ledger.shared.Amount;

public class Transaction {
    private Long id;
    private Account account;
    private Amount amount;
    private TransactionType transactionType;

    public Transaction() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }
}
