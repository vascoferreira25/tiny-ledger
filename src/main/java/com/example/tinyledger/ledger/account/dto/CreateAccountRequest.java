package com.example.tinyledger.ledger.account.dto;

import java.math.BigDecimal;

public class CreateAccountRequest {
    public BigDecimal startingBalance;

    public CreateAccountRequest() {
    }

    public BigDecimal getStartingBalance() {
        return startingBalance;
    }

    public void setStartingBalance(BigDecimal startingBalance) {
        this.startingBalance = startingBalance;
    }
}
