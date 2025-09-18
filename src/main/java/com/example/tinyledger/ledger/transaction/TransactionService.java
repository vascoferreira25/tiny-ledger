package com.example.tinyledger.ledger.transaction;

import com.example.tinyledger.ledger.account.Account;
import com.example.tinyledger.ledger.account.AccountRepository;
import com.example.tinyledger.ledger.transaction.dto.NewTransactionRequest;
import com.example.tinyledger.ledger.transaction.dto.TransactionResponse;
import com.example.tinyledger.shared.TinyLedgerInvalidArgumentException;
import com.example.tinyledger.shared.TinyLedgerNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionService {
    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    public TransactionResponse createNewTransaction(Long accountId, NewTransactionRequest newTransaction) {
        Account account = accountRepository.findAccountById(accountId).orElseThrow(() -> {
            logger.warn("Account with id {} not found", accountId);
            return new TinyLedgerNotFoundException("Account not found");
        });

        // BigDecimal compareTo returns either -1, 0, or 1
        if (newTransaction.getAmount().compareTo(BigDecimal.ZERO) < 1) {
            throw new TinyLedgerInvalidArgumentException("Amount of transaction should be greater than zero.");
        }

        if (newTransaction.getType() == TransactionType.WITHDRAWAL
                && newTransaction.getAmount().compareTo(account.getBalance()) > 0) {
            logger.warn("Account id {} doesn't have enough funds for withdrawing {}", accountId, newTransaction.getAmount());
            throw new TinyLedgerInvalidArgumentException("Not enough funds on the account.");
        }

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(newTransaction.getAmount());
        transaction.setTransactionType(newTransaction.getType());
        transaction = transactionRepository.save(transaction);

        if (transaction.getTransactionType() == TransactionType.DEPOSIT) {
            account.setBalance(account.getBalance().add(transaction.getAmount()));
        } else {
            account.setBalance(account.getBalance().subtract(transaction.getAmount()));
        }

        return transactionToResponse(transaction);
    }

    private TransactionResponse transactionToResponse(Transaction transaction) {
        TransactionResponse response = new TransactionResponse();
        response.setId(transaction.getId());
        response.setAmount(transaction.getAmount());
        response.setType(transaction.getTransactionType());
        return response;
    }

    public List<TransactionResponse> getTransactionsByAccount(Long accountId) {
        Account account = accountRepository.findAccountById(accountId).orElseThrow(() -> {
            logger.warn("Account with id {} not found", accountId);
            return new TinyLedgerNotFoundException("Account not found");
        });

        return transactionRepository.findAllTransactionsByAccount(account).stream()
                .map(this::transactionToResponse)
                .toList();
    }
}
