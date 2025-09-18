package com.example.tinyledger.shared;

public class TinyLedgerNotFoundException extends RuntimeException {
    public TinyLedgerNotFoundException(String message) {
        super(message);
    }
}
