package com.financialhub.app.services;

import com.financialhub.app.entities.Account;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    List<Account> getAllAccounts();
    Optional<Account> getAccountById(Long id);
    void createAccount(Account account);
    void updateAccount(Long id, Account updatedAccount);
    void deleteAccount(Long id);
}
