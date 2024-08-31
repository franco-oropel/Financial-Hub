package com.financialhub.app.services.impl;

import com.financialhub.app.dto.request.TransactionRequestDto;
import com.financialhub.app.dto.response.TransactionResponseDto;
import com.financialhub.app.entities.Account;
import com.financialhub.app.entities.Transaction;
import com.financialhub.app.exceptions.TransactionException;
import com.financialhub.app.mapper.TransactionMapper;
import com.financialhub.app.repositories.AccountRepository;
import com.financialhub.app.repositories.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final AccountRepository accountRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository, TransactionMapper transactionMapper, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
        this.accountRepository = accountRepository;
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
    public Optional<TransactionResponseDto> createTransaction(TransactionRequestDto transactionRequestDto) throws TransactionException {
        // Account Validation
        Account account = accountRepository.findById(transactionRequestDto.getAccount().getId()).orElseThrow(() -> new TransactionException("Account not found"));

        // Balance Account validation based on transaction type
        String transactionType = transactionRequestDto.getType().toLowerCase();
        if (transactionType.equalsIgnoreCase("deposit") || transactionType.equalsIgnoreCase("collection")) {
            account.setBalance(account.getBalance() + transactionRequestDto.getAmount());
        } else {
            // transactionType = "withdrawal" || "transfer" || "payment"
            if (account.getBalance() < transactionRequestDto.getAmount()) {
                throw new TransactionException("Insufficient account balance to " + transactionType);
            }
            account.setBalance(account.getBalance() - transactionRequestDto.getAmount());
        }

        Transaction transaction = transactionMapper.mapToEntity(transactionRequestDto);
        Transaction createdTransaction = transactionRepository.save(transaction);
        accountRepository.save(account);
        return getTransactionById(createdTransaction.getId());
    };

    private List<TransactionResponseDto> mapToDTOList(List<Transaction> transactions){
        return transactions.stream()
                //.map(a -> accountMapper.mapToDto(a))
                .map(transactionMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
