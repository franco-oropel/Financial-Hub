package com.financialhub.app.services.impl;

import com.financialhub.app.dto.request.TransactionRequestDto;
import com.financialhub.app.dto.response.TransactionResponseDto;
import com.financialhub.app.exceptions.TransactionException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionService {
    List<TransactionResponseDto> getAllTransactions();
    Optional<TransactionResponseDto> getTransactionById(Long id);
    List<TransactionResponseDto> getTransactionsByDateRangeAndAccount(Long accountId, LocalDateTime startDate, LocalDateTime endDate);
    Optional<TransactionResponseDto> createTransaction(TransactionRequestDto transactionRequestDto) throws TransactionException;
}
