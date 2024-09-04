package com.financialhub.app.services;

import com.financialhub.app.dto.request.AccountRequestDto;
import com.financialhub.app.dto.response.AccountResponseDto;
import com.financialhub.app.exceptions.AccountException;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    List<AccountResponseDto> getAllAccounts();
    Optional<AccountResponseDto> getAccountById(Long id);
    List<AccountResponseDto> getAccountsByAccountHolderName(String accountHolderName);
    Optional<AccountResponseDto> createAccount(AccountRequestDto accountRequestDto) throws AccountException;
    AccountResponseDto updateAccount(Long id, AccountRequestDto updatedAccountRequestDto) throws AccountException;
    void deleteAccount(Long id);
}
