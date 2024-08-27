package com.financialhub.app.services.impl;

import com.financialhub.app.entities.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionService {
    List<Transaction> getAllTransactions();
    Optional<Transaction> getTransactionById(Long id);
    void createTransaction(Transaction transaction);
    void updateTransaction(Long id, Transaction updatedTransaction);
    void deleteTransaction(Long id);
}
