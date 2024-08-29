package com.financialhub.app.services.impl;

import com.financialhub.app.dto.request.TransactionRequestDto;
import com.financialhub.app.dto.response.TransactionResponseDto;
import com.financialhub.app.entities.Transaction;
import com.financialhub.app.mapper.TransactionMapper;
import com.financialhub.app.repositories.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    public TransactionServiceImpl(TransactionRepository transactionRepository, TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
    }

    @Override
    public List<TransactionResponseDto> getAllTransactions(){
        List<Transaction> transactions = (List<Transaction>) transactionRepository.findAll();
        return mapToDTOList(transactions);
    }

    @Override
    public Optional<TransactionResponseDto> getTransactionById(Long id){
        Optional<Transaction> transaction = transactionRepository.findById(id);
        // Map only if the Optional has a present value
        return transaction.map(transactionMapper::mapToDto);
    };

    @Override
    public void createTransaction(TransactionRequestDto transactionRequestDto){
        Transaction transaction = transactionMapper.mapToEntity(transactionRequestDto);
        transactionRepository.save(transaction);
    };

    @Override
    public void updateTransaction(Long id, TransactionRequestDto updatedTransactionRequestDto){
        Optional<Transaction> optionalTransaction = transactionRepository.findById(id);

        if (optionalTransaction.isPresent()){
            Transaction transaction = optionalTransaction.get();
            transaction.setType(updatedTransactionRequestDto.getType());
            transaction.setAmount(updatedTransactionRequestDto.getAmount());
            transaction.setDate(updatedTransactionRequestDto.getDate());

            this.transactionRepository.save(transaction);
        }
    };

    @Override
    public void deleteTransaction(Long id){
        this.transactionRepository.deleteById(id);
    };

    private List<TransactionResponseDto> mapToDTOList(List<Transaction> transactions){
        return transactions.stream()
                //.map(a -> accountMapper.mapToDto(a))
                .map(transactionMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
