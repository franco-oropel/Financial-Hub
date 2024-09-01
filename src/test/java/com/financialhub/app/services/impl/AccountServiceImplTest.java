package com.financialhub.app.services.impl;

import com.financialhub.app.dto.request.AccountRequestDto;
import com.financialhub.app.dto.response.AccountResponseDto;
import com.financialhub.app.entities.Account;
import com.financialhub.app.exceptions.AccountException;
import com.financialhub.app.mapper.AccountMapper;
import com.financialhub.app.repositories.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private AccountServiceImpl accountService;

    private Account account;
    private AccountRequestDto accountRequestDto;
    private AccountResponseDto accountResponseDto;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setId(1L);
        account.setAccountHolderName("John Doe");
        account.setType("Savings");
        account.setBalance(1000.0);

        accountRequestDto = new AccountRequestDto();
        accountRequestDto.setAccountHolderName("John Doe");
        accountRequestDto.setType("Savings");
        accountRequestDto.setBalance(1000.0);

        accountResponseDto = new AccountResponseDto();
        accountResponseDto.setId(1L);
        accountResponseDto.setAccountHolderName("John Doe");
        accountResponseDto.setType("Savings");
        accountResponseDto.setBalance(1000.0);
    }

    @Test
    @DisplayName("getAllAccounts: should return all accounts successfully")
    void testGetAllAccounts() {
        when(accountRepository.findAll()).thenReturn(List.of(account));
        when(accountMapper.mapToDto(account)).thenReturn(accountResponseDto);

        List<AccountResponseDto> result = accountService.getAllAccounts();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(accountRepository, times(1)).findAll();
        verify(accountMapper, times(1)).mapToDto(account);
    }

    @Test
    @DisplayName("getAccountById: should return account when found by ID")
    void testGetAccountByIdFound() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountMapper.mapToDto(account)).thenReturn(accountResponseDto);

        Optional<AccountResponseDto> result = accountService.getAccountById(1L);

        assertTrue(result.isPresent());
        assertEquals(accountResponseDto, result.get());
        verify(accountRepository, times(1)).findById(1L);
        verify(accountMapper, times(1)).mapToDto(account);
    }

    @Test
    @DisplayName("getAccountById: not found")
    void testGetAccountByIdNotFound() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<AccountResponseDto> result = accountService.getAccountById(1L);

        assertFalse(result.isPresent());
        verify(accountRepository, times(1)).findById(1L);
        verify(accountMapper, never()).mapToDto(any());
    }

    @Test
    @DisplayName("createAccount: should create successfully")
    void testCreateAccountSuccess() throws AccountException {
        when(accountRepository.existsByTypeAndAccountHolderName("Savings", "John Doe")).thenReturn(false);
        when(accountMapper.mapToEntity(accountRequestDto)).thenReturn(account);
        when(accountRepository.save(account)).thenReturn(account);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountMapper.mapToDto(account)).thenReturn(accountResponseDto);

        Optional<AccountResponseDto> result = accountService.createAccount(accountRequestDto);

        assertTrue(result.isPresent());
        assertEquals(accountResponseDto, result.get());
        verify(accountRepository, times(1)).existsByTypeAndAccountHolderName("Savings", "John Doe");
        verify(accountRepository, times(1)).save(account);
        verify(accountMapper, times(1)).mapToEntity(accountRequestDto);
        verify(accountMapper, times(1)).mapToDto(account);
    }

    @Test
    @DisplayName("createAccount: should throw exception when creating an account that already exists")
    void testCreateAccountException() {
        when(accountRepository.existsByTypeAndAccountHolderName("Savings", "John Doe")).thenReturn(true);

        AccountException exception = assertThrows(AccountException.class, () -> accountService.createAccount(accountRequestDto));

        assertEquals("Account with the same type and name of holder already exists.", exception.getMessage());
        verify(accountRepository, times(1)).existsByTypeAndAccountHolderName("Savings", "John Doe");
        verify(accountRepository, never()).save(any());
        verify(accountMapper, never()).mapToEntity(any());
    }

    @Test
    @DisplayName("updateAccount: should update account successfully")
    void testUpdateAccountSuccess() throws AccountException {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountMapper.mapToDto(any(Account.class))).thenReturn(accountResponseDto);

        AccountResponseDto result = accountService.updateAccount(1L, accountRequestDto);

        assertNotNull(result);
        assertEquals(accountResponseDto, result);
        verify(accountRepository, times(1)).findById(1L);
        verify(accountRepository, times(1)).save(any(Account.class));
        verify(accountMapper, times(1)).mapToDto(any(Account.class));
    }

    @Test
    @DisplayName("updateAccount: should throw exception when updating a non-existent account")
    void testUpdateAccountNotFound() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        AccountException exception = assertThrows(AccountException.class, () -> accountService.updateAccount(1L, accountRequestDto));

        assertEquals("Account not found", exception.getMessage());
        verify(accountRepository, times(1)).findById(1L);
        verify(accountRepository, never()).save(any());
        verify(accountMapper, never()).mapToDto(any());
    }

    @Test
    @DisplayName("deleteAccount: should delete account successfully")
    void testDeleteAccount() {
        doNothing().when(accountRepository).deleteById(1L);

        accountService.deleteAccount(1L);

        verify(accountRepository, times(1)).deleteById(1L);
    }
}