package com.financialhub.app.dto.response;

import com.financialhub.app.entities.Transaction;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class AccountResponseDto {
    private Long id;
    private String accountHolderName;
    private Double balance;
    private Date openingDate;

    private List<Transaction> transactions;
}
