package com.financialhub.app.services;

import com.financialhub.app.dto.request.AccountRequestDto;
import com.financialhub.app.dto.response.AccountResponseDto;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    List<AccountResponseDto> getAllAccounts();
    Optional<AccountResponseDto> getAccountById(Long id);
    Optional<AccountResponseDto> createAccount(AccountRequestDto accountRequestDto);
    AccountResponseDto updateAccount(Long id, AccountRequestDto updatedAccountRequestDto);
    void deleteAccount(Long id);
}
