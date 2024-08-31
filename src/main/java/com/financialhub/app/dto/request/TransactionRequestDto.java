package com.financialhub.app.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.financialhub.app.entities.Account;
import com.financialhub.app.validations.transaction.ValidTransactionType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionRequestDto {
    @NotNull(message = "type is required")
    @ValidTransactionType // Custom Annotation
    private String type;

    @NotNull(message = "amount is required")
    @Min(value = 0, message = "The balance cannot be negative")
    private Double amount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;

    private Account account;
}
