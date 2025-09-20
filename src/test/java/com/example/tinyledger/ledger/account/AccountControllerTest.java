package com.example.tinyledger.ledger.account;

import com.example.tinyledger.ledger.account.dto.AccountResponse;
import com.example.tinyledger.ledger.account.dto.CreateAccountRequest;
import com.example.tinyledger.ledger.transaction.TransactionType;
import com.example.tinyledger.ledger.transaction.dto.NewTransactionRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Hamcrest matchers


@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Create an account with a valid balance")
    void createAccount_valid() throws Exception {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setStartingBalance(BigDecimal.valueOf(100.0));

        String accountJson = objectMapper.writeValueAsString(request);

        mvc.perform(post("/api/v1/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", not(Matchers.empty())))
                .andExpect(jsonPath("$.balance", is(100.0)));
    }

    @Test
    @DisplayName("Create an account with an invalid balance")
    void createAccount_invalid() throws Exception {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setStartingBalance(BigDecimal.valueOf(-100.0));

        String accountJson = objectMapper.writeValueAsString(request);

        mvc.perform(post("/api/v1/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountJson))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail", is("Amount must be equal or greater than zero.")));
    }

    @Test
    @DisplayName("Check balance of an account that doesn't exist")
    void checkBalance_invalid() throws Exception {
        mvc.perform(get("/api/v1/account/{id}/balance", Long.MAX_VALUE))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail", is("Account not found")));
    }

    @Test
    @DisplayName("Deposit positive amount to an account")
    void depositAmount_valid() throws Exception {
        // Create Account
        CreateAccountRequest accountRequest = new CreateAccountRequest();
        accountRequest.setStartingBalance(BigDecimal.valueOf(100.0));

        String accountJson = objectMapper.writeValueAsString(accountRequest);

        String responseJson = mvc.perform(post("/api/v1/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        AccountResponse accountResponse = objectMapper.readValue(responseJson, AccountResponse.class);

        // Create Transaction
        NewTransactionRequest transactionRequest = new NewTransactionRequest();
        transactionRequest.setAmount(BigDecimal.valueOf(100.0));
        transactionRequest.setType(TransactionType.DEPOSIT);

        String transactionJson = objectMapper.writeValueAsString(transactionRequest);

        mvc.perform(post("/api/v1/account/{id}/transaction", accountResponse.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transactionJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Check balance
        mvc.perform(get("/api/v1/account/{id}/balance", accountResponse.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(accountResponse.getId().intValue())))
                .andExpect(jsonPath("$.balance", is(200.0)));
    }

    @Test
    @DisplayName("Deposit negative amount to an account gives error")
    void depositAmount_invalid() throws Exception {
        // Create Account
        CreateAccountRequest accountRequest = new CreateAccountRequest();
        accountRequest.setStartingBalance(BigDecimal.valueOf(100.0));

        String accountJson = objectMapper.writeValueAsString(accountRequest);

        String responseJson = mvc.perform(post("/api/v1/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        AccountResponse accountResponse = objectMapper.readValue(responseJson, AccountResponse.class);

        // Create Transaction
        NewTransactionRequest transactionRequest = new NewTransactionRequest();
        transactionRequest.setAmount(BigDecimal.valueOf(-100.0));
        transactionRequest.setType(TransactionType.DEPOSIT);

        String transactionJson = objectMapper.writeValueAsString(transactionRequest);

        mvc.perform(post("/api/v1/account/{id}/transaction", accountResponse.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transactionJson))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail", is("Amount must be equal or greater than zero.")));

        // Check balance
        mvc.perform(get("/api/v1/account/{id}/balance", accountResponse.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(accountResponse.getId().intValue())))
                .andExpect(jsonPath("$.balance", is(100.0)));
    }

    @Test
    @DisplayName("Withdraw positive amount from an account")
    void withdrawAmount_valid() throws Exception {
        // Create Account
        CreateAccountRequest accountRequest = new CreateAccountRequest();
        accountRequest.setStartingBalance(BigDecimal.valueOf(100.0));

        String accountJson = objectMapper.writeValueAsString(accountRequest);

        String responseJson = mvc.perform(post("/api/v1/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        AccountResponse accountResponse = objectMapper.readValue(responseJson, AccountResponse.class);

        // Create Transaction
        NewTransactionRequest transactionRequest = new NewTransactionRequest();
        transactionRequest.setAmount(BigDecimal.valueOf(100.0));
        transactionRequest.setType(TransactionType.WITHDRAWAL);

        String transactionJson = objectMapper.writeValueAsString(transactionRequest);

        mvc.perform(post("/api/v1/account/{id}/transaction", accountResponse.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transactionJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Check balance
        mvc.perform(get("/api/v1/account/{id}/balance", accountResponse.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(accountResponse.getId().intValue())))
                .andExpect(jsonPath("$.balance", is(0.0)));
    }

    @Test
    @DisplayName("Withdraw negative amount from an account gives error")
    void withdrawAmount_invalid() throws Exception {
        // Create Account
        CreateAccountRequest accountRequest = new CreateAccountRequest();
        accountRequest.setStartingBalance(BigDecimal.valueOf(100.0));

        String accountJson = objectMapper.writeValueAsString(accountRequest);

        String responseJson = mvc.perform(post("/api/v1/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        AccountResponse accountResponse = objectMapper.readValue(responseJson, AccountResponse.class);

        // Create Transaction
        NewTransactionRequest transactionRequest = new NewTransactionRequest();
        transactionRequest.setAmount(BigDecimal.valueOf(-100.0));
        transactionRequest.setType(TransactionType.WITHDRAWAL);

        String transactionJson = objectMapper.writeValueAsString(transactionRequest);

        mvc.perform(post("/api/v1/account/{id}/transaction", accountResponse.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transactionJson))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail", is("Amount must be equal or greater than zero.")));

        // Check balance
        mvc.perform(get("/api/v1/account/{id}/balance", accountResponse.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(accountResponse.getId().intValue())))
                .andExpect(jsonPath("$.balance", is(100.0)));
    }

    @Test
    @DisplayName("Withdraw greater amount from an account gives error")
    void withdrawAmount_notEnoughFunds() throws Exception {
        // Create Account
        CreateAccountRequest accountRequest = new CreateAccountRequest();
        accountRequest.setStartingBalance(BigDecimal.valueOf(100.0));

        String accountJson = objectMapper.writeValueAsString(accountRequest);

        String responseJson = mvc.perform(post("/api/v1/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        AccountResponse accountResponse = objectMapper.readValue(responseJson, AccountResponse.class);

        // Create Transaction
        NewTransactionRequest transactionRequest = new NewTransactionRequest();
        transactionRequest.setAmount(BigDecimal.valueOf(200.0));
        transactionRequest.setType(TransactionType.WITHDRAWAL);

        String transactionJson = objectMapper.writeValueAsString(transactionRequest);

        mvc.perform(post("/api/v1/account/{id}/transaction", accountResponse.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transactionJson))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail", is("Result amount must not be negative.")));

        // Check balance
        mvc.perform(get("/api/v1/account/{id}/balance", accountResponse.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(accountResponse.getId().intValue())))
                .andExpect(jsonPath("$.balance", is(100.0)));
    }

    @Test
    @DisplayName("Get account transaction history")
    void getAccountTransactionHistory() throws Exception {
        // Create Account
        CreateAccountRequest accountRequest = new CreateAccountRequest();
        accountRequest.setStartingBalance(BigDecimal.valueOf(100.0));

        String accountJson = objectMapper.writeValueAsString(accountRequest);

        String responseJson = mvc.perform(post("/api/v1/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        AccountResponse accountResponse = objectMapper.readValue(responseJson, AccountResponse.class);

        // Deposit 200 Transaction
        NewTransactionRequest depositTransactionRequest = new NewTransactionRequest();
        depositTransactionRequest.setAmount(BigDecimal.valueOf(100.0));
        depositTransactionRequest.setType(TransactionType.DEPOSIT);

        String depositTransactionJson = objectMapper.writeValueAsString(depositTransactionRequest);

        mvc.perform(post("/api/v1/account/{id}/transaction", accountResponse.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(depositTransactionJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        mvc.perform(post("/api/v1/account/{id}/transaction", accountResponse.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(depositTransactionJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Remove 300
        NewTransactionRequest withdrawTransactionRequest = new NewTransactionRequest();
        withdrawTransactionRequest.setAmount(BigDecimal.valueOf(300.0));
        withdrawTransactionRequest.setType(TransactionType.WITHDRAWAL);

        String withdrawTransactionJson = objectMapper.writeValueAsString(withdrawTransactionRequest);

        mvc.perform(post("/api/v1/account/{id}/transaction", accountResponse.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(withdrawTransactionJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Check balance
        mvc.perform(get("/api/v1/account/{id}/balance", accountResponse.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(accountResponse.getId().intValue())))
                .andExpect(jsonPath("$.balance", is(0.0)));

        // Transaction History
        mvc.perform(get("/api/v1/account/{id}/transaction", accountResponse.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].amount", is(100.0)))
                .andExpect(jsonPath("$[0].type", is("DEPOSIT")))
                .andExpect(jsonPath("$[1].amount", is(100.0)))
                .andExpect(jsonPath("$[1].type", is("DEPOSIT")))
                .andExpect(jsonPath("$[2].amount", is(300.0)))
                .andExpect(jsonPath("$[2].type", is("WITHDRAWAL")));
    }
}