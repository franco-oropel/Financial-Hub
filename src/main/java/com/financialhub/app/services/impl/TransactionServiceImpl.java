package com.financialhub.app.services.impl;

import com.financialhub.app.entities.Transaction;
import com.financialhub.app.repositories.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;


    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public List<Transaction> getAllTransactions(){
        return (List<Transaction>) this.transactionRepository.findAll();
    }

    @Override
    public Optional<Transaction> getTransactionById(Long id){
        return this.transactionRepository.findById(id);
    };

    @Override
    public void createTransaction(Transaction transaction){
        this.transactionRepository.save(transaction);
    };

    @Override
    public void updateTransaction(Long id, Transaction updatedTransaction){
        Optional<Transaction> optionalTransaction = this.transactionRepository.findById(id);

        if (optionalTransaction.isPresent()){
            Transaction transaction = optionalTransaction.get();
            transaction.setType(updatedTransaction.getType());
            transaction.setAmount(updatedTransaction.getAmount());
            transaction.setDate(updatedTransaction.getDate());

            this.transactionRepository.save(transaction);
        }
    };

    @Override
    public void deleteTransaction(Long id){
        this.transactionRepository.deleteById(id);
    };
}
