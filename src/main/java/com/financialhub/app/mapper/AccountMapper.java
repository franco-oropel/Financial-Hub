package com.financialhub.app.mapper;

import com.financialhub.app.dto.request.AccountRequestDto;
import com.financialhub.app.dto.response.AccountResponseDto;
import com.financialhub.app.entities.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    Account mapToEntity(AccountRequestDto accountRequestDto);
    AccountResponseDto mapToDto(Account account);
}
