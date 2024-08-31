package com.financialhub.app.controllers;

import com.financialhub.app.dto.request.TransactionRequestDto;
import com.financialhub.app.dto.response.TransactionResponseDto;
import com.financialhub.app.exceptions.TransactionException;
import com.financialhub.app.services.impl.TransactionService;
import com.financialhub.app.util.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TransactionResponseDto>>> getAllTransactions(){
        try{
            List<TransactionResponseDto> transactions = transactionService.getAllTransactions();
            ApiResponse<List<TransactionResponseDto>> response = new ApiResponse<>("Transactions retrieved successfully", "success", transactions);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            ApiResponse<List<TransactionResponseDto>> response = new ApiResponse<>(e.getMessage(),"error", null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Optional<TransactionResponseDto>>> getTransactionById(@PathVariable Long id){
        try{
            Optional<TransactionResponseDto> transaction = transactionService.getTransactionById(id);
            if(transaction.isPresent()){
                ApiResponse<Optional<TransactionResponseDto>> response = new ApiResponse<>("Transaction found successfully","success", transaction);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                ApiResponse<Optional<TransactionResponseDto>> response = new ApiResponse<>("Transaction not found","success", null);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e){
            ApiResponse<Optional<TransactionResponseDto>> response = new ApiResponse<>(e.getMessage(),"error", null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Optional<TransactionResponseDto>>> createTransaction(@RequestBody @Valid TransactionRequestDto transactionRequestDto){
        try{
            Optional<TransactionResponseDto> createdTransaction = transactionService.createTransaction(transactionRequestDto);
            ApiResponse<Optional<TransactionResponseDto>> response = new ApiResponse<>("Transaction created successfully", "success", createdTransaction);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (TransactionException te) {
            ApiResponse<Optional<TransactionResponseDto>> response = new ApiResponse<>("Error creating transaction: " + te.getMessage(),"error", null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            ApiResponse<Optional<TransactionResponseDto>> response = new ApiResponse<>("Error creating transaction: " + e.getMessage(),"error", null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
