package com.example.tinyledger.ledger.transaction;

import com.example.tinyledger.ledger.account.Account;
import com.example.tinyledger.ledger.account.AccountRepository;
import com.example.tinyledger.ledger.shared.Amount;
import com.example.tinyledger.ledger.transaction.dto.NewTransactionRequest;
import com.example.tinyledger.ledger.transaction.dto.TransactionResponse;
import com.example.tinyledger.shared.TinyLedgerNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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

        Amount amount = new Amount(newTransaction.getAmount());

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setTransactionType(newTransaction.getType());
        transaction = transactionRepository.save(transaction);

        if (transaction.getTransactionType() == TransactionType.DEPOSIT) {
            account.deposit(transaction.getAmount());
        } else {
            account.withdraw(transaction.getAmount());
        }

        // As this method does not run in a (DB) transaction (as specified in the assumptions),
        // in the case this operation fails, transactions will be out of sync with the account.
        // As a solution, this method should be @Transactional.
        accountRepository.save(account);

        return transactionToResponse(transaction);
    }

    private TransactionResponse transactionToResponse(Transaction transaction) {
        TransactionResponse response = new TransactionResponse();
        response.setId(transaction.getId());
        response.setAmount(transaction.getAmount().value());
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
