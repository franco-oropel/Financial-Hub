package com.financialhub.app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financialhub.app.dto.request.TransactionRequestDto;
import com.financialhub.app.dto.response.TransactionResponseDto;
import com.financialhub.app.entities.Account;
import com.financialhub.app.exceptions.TransactionException;
import com.financialhub.app.services.impl.TransactionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
class TransactionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/v1/transactions - Success")
    void testGetAllTransactionsSuccess() throws Exception {
        // Arrange
        TransactionResponseDto transaction1 = new TransactionResponseDto();
        transaction1.setId(1L);
        transaction1.setType("deposit");
        transaction1.setAmount(500.0);
        transaction1.setAccount(new Account());

        TransactionResponseDto transaction2 = new TransactionResponseDto();
        transaction2.setId(2L);
        transaction2.setType("withdrawal");
        transaction2.setAmount(200.0);
        transaction2.setAccount(new Account());

        List<TransactionResponseDto> transactions = Arrays.asList(transaction1, transaction2);

        when(transactionService.getAllTransactions()).thenReturn(transactions);

        // Act & Assert
        mockMvc.perform(get("/api/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Transactions retrieved successfully"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(transactions.size()));
    }

    @Test
    @DisplayName("GET /api/v1/transactions/{id} - Found")
    void testGetTransactionByIdFound() throws Exception {
        // Arrange
        Long transactionId = 1L;
        TransactionResponseDto transactionResponseDto = new TransactionResponseDto();
        transactionResponseDto.setId(transactionId);
        transactionResponseDto.setType("deposit");
        transactionResponseDto.setAmount(500.0);
        transactionResponseDto.setAccount(new Account());

        when(transactionService.getTransactionById(transactionId)).thenReturn(Optional.of(transactionResponseDto));

        // Act & Assert
        mockMvc.perform(get("/api/v1/transactions/{id}", transactionId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Transaction found successfully"))
                .andExpect(jsonPath("$.data.id").value(transactionId));
    }

    @Test
    @DisplayName("GET /api/v1/transactions/{id} - Not Found")
    void testGetTransactionByIdNotFound() throws Exception {
        // Arrange
        Long transactionId = 1L;
        when(transactionService.getTransactionById(transactionId)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/v1/transactions/{id}", transactionId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Transaction not found"));
    }

    @Test
    @DisplayName("GET /api/v1/transactions/searchBy - Success")
    void testGetTransactionsByDateRangeAndAccount() throws Exception {
        // Arrange
        Long accountId = 1L;
        LocalDateTime startDate = LocalDateTime.parse("2021-01-01T00:00:00");
        LocalDateTime endDate = LocalDateTime.parse("2021-12-31T23:59:59");

        TransactionResponseDto transaction1 = new TransactionResponseDto();
        transaction1.setId(1L);
        transaction1.setType("deposit");
        transaction1.setAmount(500.0);
        transaction1.setDate(LocalDateTime.parse("2021-06-15T10:15:30"));
        transaction1.setAccount(new Account());

        TransactionResponseDto transaction2 = new TransactionResponseDto();
        transaction2.setId(2L);
        transaction2.setType("withdrawal");
        transaction2.setAmount(200.0);
        transaction2.setDate(LocalDateTime.parse("2021-07-20T14:20:45"));
        transaction2.setAccount(new Account());

        List<TransactionResponseDto> transactions = Arrays.asList(transaction1, transaction2);

        when(transactionService.getTransactionsByDateRangeAndAccount(accountId, startDate, endDate))
                .thenReturn(transactions);

        // Act & Assert
        mockMvc.perform(get("/api/v1/transactions/searchBy")
                        .param("accountId", accountId.toString())
                        .param("startDate", "2021-01-01 00:00:00")
                        .param("endDate", "2021-12-31 23:59:59")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Transactions found successfully"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(transactions.size()))
                .andExpect(jsonPath("$.data[0].id").value(transaction1.getId()))
                .andExpect(jsonPath("$.data[0].type").value(transaction1.getType()))
                .andExpect(jsonPath("$.data[0].amount").value(transaction1.getAmount()))
                .andExpect(jsonPath("$.data[0].date").value("2021-06-15 10:15:30"))
                .andExpect(jsonPath("$.data[1].id").value(transaction2.getId()))
                .andExpect(jsonPath("$.data[1].type").value(transaction2.getType()))
                .andExpect(jsonPath("$.data[1].amount").value(transaction2.getAmount()))
                .andExpect(jsonPath("$.data[1].date").value("2021-07-20 14:20:45"));
    }

    @Test
    @DisplayName("POST /api/v1/transactions - Success")
    void testCreateTransactionSuccess() throws Exception {
        // Arrange
        Account account = new Account();
        account.setId(1L);

        TransactionRequestDto transactionRequestDto = new TransactionRequestDto();
        transactionRequestDto.setType("deposit");
        transactionRequestDto.setAmount(500.0);
        transactionRequestDto.setAccount(account);

        TransactionResponseDto transactionResponseDto = new TransactionResponseDto();
        transactionResponseDto.setId(1L);
        transactionResponseDto.setType("deposit");
        transactionResponseDto.setAmount(500.0);
        transactionResponseDto.setAccount(account);

        when(transactionService.createTransaction(any(TransactionRequestDto.class)))
                .thenReturn(Optional.of(transactionResponseDto));

        // Act & Assert
        mockMvc.perform(post("/api/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Transaction created successfully"))
                .andExpect(jsonPath("$.data.id").value(1L));
    }

    @Test
    @DisplayName("POST /api/v1/transactions - TransactionException")
    void testCreateTransactionFailureDueToTransactionException() throws Exception {
        // Arrange
        Account account = new Account();
        account.setId(1L);

        TransactionRequestDto transactionRequestDto = new TransactionRequestDto();
        transactionRequestDto.setType("withdrawal");
        transactionRequestDto.setAmount(1000.0);
        transactionRequestDto.setAccount(account);

        when(transactionService.createTransaction(any(TransactionRequestDto.class)))
                .thenThrow(new TransactionException("Insufficient account balance to withdrawal"));

        // Act & Assert
        mockMvc.perform(post("/api/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("Error creating transaction: Insufficient account balance to withdrawal"));
    }
}

