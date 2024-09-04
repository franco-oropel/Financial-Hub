package com.financialhub.app.services.impl;

import com.financialhub.app.dto.request.TransactionRequestDto;
import com.financialhub.app.dto.response.TransactionResponseDto;
import com.financialhub.app.entities.Account;
import com.financialhub.app.entities.Transaction;
import com.financialhub.app.exceptions.TransactionException;
import com.financialhub.app.mapper.TransactionMapper;
import com.financialhub.app.repositories.AccountRepository;
import com.financialhub.app.repositories.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("getAllTransactions: should return all transactions successfully")
    void testGetAllTransactionsSuccess() {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setType("deposit");
        TransactionResponseDto transactionResponseDto = new TransactionResponseDto();
        transactionResponseDto.setId(1L);
        transactionResponseDto.setType("deposit");

        when(transactionRepository.findAll()).thenReturn(List.of(transaction));
        when(transactionMapper.mapToDto(transaction)).thenReturn(transactionResponseDto);

        List<TransactionResponseDto> result = transactionService.getAllTransactions();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("deposit", result.get(0).getType());
    }

    @Test
    @DisplayName("getTransactionById: should return transaction when found by ID")
    void testGetTransactionByIdFound() {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setType("deposit");
        TransactionResponseDto transactionResponseDto = new TransactionResponseDto();
        transactionResponseDto.setId(1L);
        transactionResponseDto.setType("deposit");

        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        when(transactionMapper.mapToDto(transaction)).thenReturn(transactionResponseDto);

        Optional<TransactionResponseDto> result = transactionService.getTransactionById(1L);

        assertTrue(result.isPresent());
        assertEquals("deposit", result.get().getType());
    }

    @Test
    @DisplayName("getTransactionById: not Found")
    void testGetTransactionByIdNotFound() {
        when(transactionRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<TransactionResponseDto> result = transactionService.getTransactionById(1L);

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("getTransactionsByDateRangeAndAccount: should return transactions successfully")
    void testGetTransactionsByDateRangeAndAccount() {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setType("deposit");
        TransactionResponseDto transactionResponseDto = new TransactionResponseDto();
        transactionResponseDto.setId(1L);
        transactionResponseDto.setType("deposit");

        when(transactionRepository.findTransactionsByDateRangeAndAccount(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(List.of(transaction));
        when(transactionMapper.mapToDto(transaction)).thenReturn(transactionResponseDto);

        List<TransactionResponseDto> result = transactionService.getTransactionsByDateRangeAndAccount(1L, LocalDateTime.now().minusDays(1), LocalDateTime.now());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("deposit", result.get(0).getType());
    }

    @Test
    @DisplayName("createTransaction: should create successfully")
    void testCreateTransactionSuccess() throws TransactionException {
        // Request DTO
        TransactionRequestDto transactionRequestDto = new TransactionRequestDto();
        transactionRequestDto.setType("deposit");
        transactionRequestDto.setAmount(500.0);
        transactionRequestDto.setDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

        // Entity Account
        Account account = new Account();
        account.setId(1L);
        account.setAccountHolderName("John Doe");
        account.setType("Savings");
        account.setBalance(1000.0);
        transactionRequestDto.setAccount(account);

        // Entity Transaction
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setType("deposit");
        transaction.setAmount(500.0);
        transaction.setDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        transaction.setAccount(account);

        // Response DTO
        TransactionResponseDto transactionResponseDto = new TransactionResponseDto();
        transactionResponseDto.setId(1L);
        transactionResponseDto.setType("deposit");
        transactionResponseDto.setAmount(500.0);
        transactionResponseDto.setDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        transactionResponseDto.setAccount(account);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(transactionMapper.mapToEntity(transactionRequestDto)).thenReturn(transaction);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        when(accountRepository.save(account)).thenReturn(account);
        when(transactionMapper.mapToDto(transaction)).thenReturn(transactionResponseDto);

        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        when(transactionMapper.mapToDto(transaction)).thenReturn(transactionResponseDto);

        Optional<TransactionResponseDto> result = transactionService.createTransaction(transactionRequestDto);
        assertTrue(result.isPresent());
        assertEquals("deposit", result.get().getType());

        verify(accountRepository).save(account);
    }

    @Test
    @DisplayName("createTransaction: should throw exception when the account not found")
    void testCreateTransactionAccountNotFound() {
        TransactionRequestDto transactionRequestDto = new TransactionRequestDto();
        transactionRequestDto.setType("deposit");
        transactionRequestDto.setAmount(500.0);
        Account account = new Account();
        account.setId(1L);
        transactionRequestDto.setAccount(account);

        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        TransactionException exception = assertThrows(TransactionException.class, () -> {
            transactionService.createTransaction(transactionRequestDto);
        });

        assertEquals("Account not found", exception.getMessage());
    }

    @Test
    @DisplayName("createTransaction: should throw exception when the account balance is insufficient")
    void testCreateTransactionInsufficientBalance() {
        TransactionRequestDto transactionRequestDto = new TransactionRequestDto();
        transactionRequestDto.setType("withdrawal");
        transactionRequestDto.setAmount(1500.0);
        Account account = new Account();
        account.setId(1L);
        account.setBalance(1000.0);
        transactionRequestDto.setAccount(account);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        TransactionException exception = assertThrows(TransactionException.class, () -> {
            transactionService.createTransaction(transactionRequestDto);
        });

        assertEquals("Insufficient account balance to withdrawal", exception.getMessage());
    }
}
