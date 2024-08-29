package com.financialhub.app.mapper;

import com.financialhub.app.dto.request.TransactionRequestDto;
import com.financialhub.app.dto.response.TransactionResponseDto;
import com.financialhub.app.entities.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    Transaction mapToEntity(TransactionRequestDto transactionRequestDto);
    TransactionResponseDto mapToDto(Transaction transaction);
}
