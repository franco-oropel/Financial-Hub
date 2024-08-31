package com.financialhub.app.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.financialhub.app.entities.Transaction;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AccountResponseDto {
    private Long id;
    private String accountHolderName;
    private String type;
    private Double balance;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime openingDate;

    private List<Transaction> transactions;
}
