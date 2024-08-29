package com.financialhub.app.services.impl;

import com.financialhub.app.dto.request.TransactionRequestDto;
import com.financialhub.app.dto.response.TransactionResponseDto;

import java.util.List;
import java.util.Optional;

public interface TransactionService {
    List<TransactionResponseDto> getAllTransactions();
    Optional<TransactionResponseDto> getTransactionById(Long id);
    void createTransaction(TransactionRequestDto transactionRequestDto);
    void updateTransaction(Long id, TransactionRequestDto updatedTransactionRequestDto);
    void deleteTransaction(Long id);
}
