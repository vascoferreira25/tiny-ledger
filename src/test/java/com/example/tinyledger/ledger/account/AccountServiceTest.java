package com.example.tinyledger.ledger.account;

import com.example.tinyledger.ledger.account.dto.AccountResponse;
import com.example.tinyledger.ledger.account.dto.CreateAccountRequest;
import com.example.tinyledger.shared.TinyLedgerInvalidArgumentException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@DisplayName("Test Account Service")
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
    @DisplayName("Creating an account with negative balance should throw Exception")
    void createAccount_negativeBalance_throwException() {
        // Given
        CreateAccountRequest newAccount = new CreateAccountRequest();
        newAccount.setStartingBalance(BigDecimal.valueOf(-1));

        // Then
        assertThatExceptionOfType(TinyLedgerInvalidArgumentException.class)
                .isThrownBy(() -> accountService.createAccount(newAccount))
                .withMessage("Balance must not be negative");
    }

    @Test
    @DisplayName("Creating an account with zero balance should be successful")
    void createAccount_zeroBalance_throwException() {
        // Given
        CreateAccountRequest newAccount = new CreateAccountRequest();
        newAccount.setStartingBalance(BigDecimal.ZERO);

        Account account = new Account();
        account.setId(1L);
        account.setBalance(BigDecimal.ZERO);

        // When
        when(accountRepository.save(any())).thenReturn(account);
        AccountResponse response = accountService.createAccount(newAccount);

        // Then
        verify(accountRepository, times(1)).save(any());

        assertThat(response.getId())
                .isNotNull();

        assertThat(response.getBalance())
                .isZero();
    }
}