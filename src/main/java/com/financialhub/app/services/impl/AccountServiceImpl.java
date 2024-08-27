package com.financialhub.app.services.impl;

import com.financialhub.app.entities.Account;
import com.financialhub.app.repositories.AccountRepository;
import com.financialhub.app.services.AccountService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public List<Account> getAllAccounts() {
        return (List<Account>) this.accountRepository.findAll();
    }

    @Override
    public Optional<Account> getAccountById(Long id) {
        return this.accountRepository.findById(id);
    }

    @Override
    public void createAccount(Account account) {
        this.accountRepository.save(account);
    }

    @Override
    public void updateAccount(Long id, Account updatedAccount) {
        Optional<Account> optionalAccount = this.accountRepository.findById(id);

        if (optionalAccount.isPresent()){
           Account account = optionalAccount.get();
           account.setAccountHolderName(updatedAccount.getAccountHolderName());
           account.setBalance(updatedAccount.getBalance());
           account.setOpeningDate(updatedAccount.getOpeningDate());

           this.accountRepository.save(account);
        }
    }

    @Override
    public void deleteAccount(Long id) {
        this.accountRepository.deleteById(id);
    }
}
