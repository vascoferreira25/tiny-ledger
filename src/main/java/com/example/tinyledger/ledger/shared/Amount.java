package com.example.tinyledger.ledger.shared;

import com.example.tinyledger.shared.TinyLedgerInvalidArgumentException;

import java.math.BigDecimal;

public record Amount(BigDecimal value) implements Comparable<Amount> {
    public Amount {
        validateAmount(value);
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null)
            throw new TinyLedgerInvalidArgumentException("Amount must not be null.");

        if (amount.compareTo(BigDecimal.ZERO) < 0)
            throw new TinyLedgerInvalidArgumentException("Amount must be equal or greater than zero.");
    }

    public Amount add(Amount amount) {
        BigDecimal value = this.value.add(amount.value);
        return new Amount(value);
    }

    public Amount subtract(Amount amount) {
        if (this.isLessThan(amount))
            throw new TinyLedgerInvalidArgumentException("Result amount must not be negative.");

        BigDecimal value = this.value.subtract(amount.value);
        return new Amount(value);
    }

    public boolean isGreaterThan(Amount amount) {
        return this.compareTo(amount) > 0;
    }

    public boolean isLessThan(Amount amount) {
        return this.compareTo(amount) < 0;
    }

    @Override
    public int compareTo(Amount o) {
        return this.value.compareTo(o.value);
    }
}
