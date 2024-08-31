package com.financialhub.app.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.financialhub.app.entities.Account;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionResponseDto {
    private Long id;
    private String type;
    private Double amount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;

    private Account account;
}
