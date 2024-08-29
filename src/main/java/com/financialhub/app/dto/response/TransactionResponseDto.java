package com.financialhub.app.dto.response;

import com.financialhub.app.entities.Account;
import lombok.Data;

import java.util.Date;

@Data
public class TransactionResponseDto {
    private Long id;
    private String type;
    private Double amount;
    private Date date;

    private Account account;
}
