package com.financialhub.app.dto.request;

import com.financialhub.app.entities.Account;
import lombok.Data;

import java.util.Date;

@Data
public class TransactionRequestDto {
    private String type;
    private Double amount;
    private Date date;

    private Account account;
}
