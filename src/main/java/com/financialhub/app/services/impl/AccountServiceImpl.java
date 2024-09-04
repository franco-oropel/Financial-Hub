package com.financialhub.app.services.impl;

import com.financialhub.app.dto.request.AccountRequestDto;
import com.financialhub.app.dto.response.AccountResponseDto;
import com.financialhub.app.entities.Account;
import com.financialhub.app.exceptions.AccountException;
import com.financialhub.app.mapper.AccountMapper;
import com.financialhub.app.repositories.AccountRepository;
import com.financialhub.app.services.AccountService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public AccountServiceImpl(AccountRepository accountRepository, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
    }

    @Override
    public List<AccountResponseDto> getAllAccounts() {
        List<Account> accounts = (List<Account>) accountRepository.findAll();
        return mapToDTOList(accounts);
    }

    @Override
    public Optional<AccountResponseDto> getAccountById(Long id) {
        Optional<Account> account = accountRepository.findById(id);
        // Map only if the Optional has a present value
        return account.map(accountMapper::mapToDto);
    }

    @Override
    public List<AccountResponseDto> getAccountsByAccountHolderName(String accountHolderName) {
        List<Account> accounts = accountRepository.findByAccountHolderName(accountHolderName);
        return mapToDTOList(accounts);
    }

    @Override
    public Optional<AccountResponseDto> createAccount(AccountRequestDto accountRequestDto) throws AccountException {

        // Validation of account uniqueness by type and name of the holder
        boolean exists = accountRepository.existsByTypeAndAccountHolderName(accountRequestDto.getType(), accountRequestDto.getAccountHolderName());
        if (exists) { throw new AccountException("Account with the same type and name of holder already exists."); }

        Account account = accountMapper.mapToEntity(accountRequestDto);
        Account createdAccount = accountRepository.save(account);
        return getAccountById(createdAccount.getId());
    }

    @Override
    public AccountResponseDto updateAccount(Long id, AccountRequestDto updatedAccountRequestDto) throws AccountException {
        Optional<Account> optionalAccount = accountRepository.findById(id);

        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            account.setAccountHolderName(updatedAccountRequestDto.getAccountHolderName());
            account.setType(updatedAccountRequestDto.getType());
            account.setBalance(updatedAccountRequestDto.getBalance());
            account.setOpeningDate(updatedAccountRequestDto.getOpeningDate());

            accountRepository.save(account);
            return accountMapper.mapToDto(account);
        } else {
            throw new AccountException("Account not found");
        }
    }

    @Override
    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }

    private List<AccountResponseDto> mapToDTOList(List<Account> accounts){
        return accounts.stream()
                //.map(a -> accountMapper.mapToDto(a))
                .map(accountMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
