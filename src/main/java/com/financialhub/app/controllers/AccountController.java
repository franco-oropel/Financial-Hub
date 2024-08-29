package com.financialhub.app.controllers;

import com.financialhub.app.dto.request.AccountRequestDto;
import com.financialhub.app.dto.response.AccountResponseDto;
import com.financialhub.app.services.AccountService;
import com.financialhub.app.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AccountResponseDto>>> getAllAccounts(){
        try {
            List<AccountResponseDto> accounts = accountService.getAllAccounts();
            ApiResponse<List<AccountResponseDto>> response = new ApiResponse<>("Accounts retrieved successfully", "success", accounts);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            ApiResponse<List<AccountResponseDto>> response = new ApiResponse<>(e.getMessage(),"error", null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Optional<AccountResponseDto>>> getAccountById(@PathVariable Long id){
        try {
            Optional<AccountResponseDto> account = accountService.getAccountById(id);
            if(account.isPresent()){
                ApiResponse<Optional<AccountResponseDto>> response = new ApiResponse<>("Account found successfully","success", account);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                ApiResponse<Optional<AccountResponseDto>> response = new ApiResponse<>("Account not found","success", null);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e){
            ApiResponse<Optional<AccountResponseDto>> response = new ApiResponse<>(e.getMessage(),"error", null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Optional<AccountResponseDto>>> createAccount(@RequestBody AccountRequestDto accountRequestDto){
        try {
            Optional<AccountResponseDto> createdAccount = accountService.createAccount(accountRequestDto);
            ApiResponse<Optional<AccountResponseDto>> response = new ApiResponse<>("Account created successfully", "success", createdAccount);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e){
            ApiResponse<Optional<AccountResponseDto>> response = new ApiResponse<>("Error creating account: " + e.getMessage(),"error", null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AccountResponseDto>> updateAccount(@PathVariable Long id, @RequestBody AccountRequestDto accountRequestDto){
        try {
            AccountResponseDto updateAccount = accountService.updateAccount(id, accountRequestDto);
            ApiResponse<AccountResponseDto> response = new ApiResponse<>("Account updated successfully", "success", updateAccount);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            ApiResponse<AccountResponseDto> response = new ApiResponse<>("Error updating account: " + e.getMessage(),"error", null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAccount(@PathVariable Long id){
        try {
            Optional<AccountResponseDto> existAccount = accountService.getAccountById(id);
            if(existAccount.isPresent()){
                accountService.deleteAccount(id);
                ApiResponse<Void> response = new ApiResponse<>("Account deleted successfully", "success", null);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                ApiResponse<Void> response = new ApiResponse<>("Account not found", "success", null);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e){
            ApiResponse<Void> response = new ApiResponse<>("Error deleting account: " + e.getMessage(), "success", null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
