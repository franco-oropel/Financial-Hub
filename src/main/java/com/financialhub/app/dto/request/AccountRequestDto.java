package com.financialhub.app.dto.request;

import lombok.Data;

import java.util.Date;

@Data
public class AccountRequestDto {
    private String accountHolderName;
    private Double balance;
    private Date openingDate;
}
