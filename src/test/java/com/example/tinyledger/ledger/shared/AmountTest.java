package com.example.tinyledger.ledger.shared;

import com.example.tinyledger.shared.TinyLedgerInvalidArgumentException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class AmountTest {

    @Test
    @DisplayName("Create an amount with value zero")
    void createAmount_zero_valid() {
        Amount a1 = new Amount(BigDecimal.ZERO);
        assertThat(a1.value())
                .isZero();
    }

    @Test
    @DisplayName("Create an amount with positive value")
    void createAmount_positive_valid() {
        Amount a1 = new Amount(BigDecimal.valueOf(100.0));
        assertThat(a1.value())
                .isEqualTo("100.0");
    }

    @Test
    @DisplayName("Create amount with negative value throws exception")
    void createAmount_negative_invalid() {
        assertThatExceptionOfType(TinyLedgerInvalidArgumentException.class)
                .isThrownBy(() -> new Amount(BigDecimal.valueOf(-100.0)))
                .withMessage("Amount must be equal or greater than zero.");
    }

    @Test
    @DisplayName("Create amount with null value throws exception")
    void createAmount_null_invalid() {
        assertThatExceptionOfType(TinyLedgerInvalidArgumentException.class)
                .isThrownBy(() -> new Amount(null))
                .withMessage("Amount must not be null.");
    }

    @Test
    @DisplayName("Add an amount to another amount - valid result")
    void add_validResult() {
        Amount a1 = new Amount(BigDecimal.ZERO);
        Amount a2 = new Amount(BigDecimal.valueOf(100.0));
        Amount r1 = a1.add(a2);
        assertThat(r1.value())
                .isEqualTo("100.0");
    }

    @Test
    @DisplayName("Add a decimal amount to another amount - valid result")
    void add_decimalValues_valid() {
        Amount a1 = new Amount(new BigDecimal("100.50"));
        Amount a2 = new Amount(new BigDecimal("50.25"));
        Amount result = a1.add(a2);
        assertThat(result.value())
                .isEqualTo(new BigDecimal("150.75"));
    }

    @Test
    @DisplayName("Add a negative value throws exception")
    void subtract_invalidResult() {
        Amount a1 = new Amount(BigDecimal.ZERO);
        Amount a2 = new Amount(BigDecimal.valueOf(100.0));
        assertThatExceptionOfType(TinyLedgerInvalidArgumentException.class)
                .isThrownBy(() -> a1.subtract(a2))
                .withMessage("Result amount must not be negative.");
    }

    @Test
    @DisplayName("Subtract amount = valid result")
    void subtract_validResult() {
        Amount a1 = new Amount(BigDecimal.valueOf(100.0));
        Amount a2 = new Amount(BigDecimal.valueOf(50.0));
        Amount r1 = a1.subtract(a2);
        assertThat(r1.value())
                .isEqualTo("50.0");
    }

    @Test
    @DisplayName("Subtract amount to zero - valid result")
    void subtract_toZero_valid() {
        Amount a1 = new Amount(new BigDecimal("100.00"));
        Amount a2 = new Amount(new BigDecimal("100.00"));
        Amount result = a1.subtract(a2);
        assertThat(result.value())
                .isEqualTo("0.00");
    }

    @Test
    void isGreaterThan() {
        Amount a1 = new Amount(BigDecimal.valueOf(100.0));
        Amount a2 = new Amount(BigDecimal.valueOf(50.0));
        assertThat(a1.isGreaterThan(a2))
                .isTrue();

        assertThat(a2.isGreaterThan(a1))
                .isFalse();
    }

    @Test
    void isLessThan() {
        Amount a1 = new Amount(BigDecimal.valueOf(100.0));
        Amount a2 = new Amount(BigDecimal.valueOf(50.0));
        assertThat(a2.isLessThan(a1))
                .isTrue();

        assertThat(a1.isLessThan(a2))
                .isFalse();
    }

    @Test
    void compareTo() {
        Amount a1 = new Amount(BigDecimal.valueOf(100.0));
        Amount a2 = new Amount(BigDecimal.valueOf(50.0));
        Amount a3 = new Amount(BigDecimal.valueOf(50.0));

        assertThat(a1.compareTo(a2))
                .isEqualTo(1);
        assertThat(a2.compareTo(a1))
                .isEqualTo(-1);
        assertThat(a2.compareTo(a3))
                .isEqualTo(0);
    }
}