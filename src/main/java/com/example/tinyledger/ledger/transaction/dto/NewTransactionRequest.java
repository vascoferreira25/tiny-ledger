package com.example.tinyledger.ledger.transaction.dto;

import com.example.tinyledger.ledger.transaction.TransactionType;

import java.math.BigDecimal;

public class NewTransactionRequest {
    private BigDecimal amount;
    private TransactionType type;

    public NewTransactionRequest() {
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }
}
