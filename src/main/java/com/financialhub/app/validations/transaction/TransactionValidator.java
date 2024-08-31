package com.financialhub.app.validations.transaction;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class TransactionValidator implements ConstraintValidator<ValidTransactionType, String> {
    @Override
    public boolean isValid(String transactionType, ConstraintValidatorContext constraintValidatorContext) {
        String[] validTypes = {"deposit", "withdrawal", "transfer", "payment", "collection"};

        return transactionType != null
                && !transactionType.isEmpty()
                && Arrays.asList(validTypes).contains(transactionType.trim().toLowerCase());
    }
}