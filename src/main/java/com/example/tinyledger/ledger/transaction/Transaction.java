package com.example.tinyledger.ledger.transaction;

import com.example.tinyledger.ledger.account.Account;
import com.example.tinyledger.shared.TinyLedgerInvalidArgumentException;

import java.math.BigDecimal;

public class Transaction {
    private Long id;
    private Account account;
    private BigDecimal amount;
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

    public BigDecimal getAmount() {
        return amount;
    }

    private void validateAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0)
            throw new TinyLedgerInvalidArgumentException("Amount must be equal or greater than zero.");
    }

    public void setAmount(BigDecimal amount) {
        validateAmount(amount);
        this.amount = amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }
}
