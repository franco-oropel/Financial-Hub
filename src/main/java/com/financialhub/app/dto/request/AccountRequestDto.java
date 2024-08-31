package com.financialhub.app.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.financialhub.app.validations.account.ValidAccountType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AccountRequestDto {
    @NotNull(message = "accountHolderName is required")
    @NotBlank(message = "accountHolderName is required")
    private String accountHolderName;

    @NotNull(message = "type is required")
    @ValidAccountType // Custom Annotation
    private String type;

    @NotNull(message = "balance is required")
    @Min(value = 0, message = "The balance cannot be negative")
    private Double balance;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime openingDate;
}
