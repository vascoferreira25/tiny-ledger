package com.example.tinyledger.ledger.transaction.dto;

import com.example.tinyledger.ledger.transaction.TransactionType;

import java.math.BigDecimal;

public class TransactionResponse {
    private Long id;
    private BigDecimal amount;
    private TransactionType type;

    public TransactionResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
