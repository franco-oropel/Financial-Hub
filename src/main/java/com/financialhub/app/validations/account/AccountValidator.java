package com.financialhub.app.validations.account;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class AccountValidator implements ConstraintValidator<ValidAccountType, String> {
    @Override
    public boolean isValid(String transactionType, ConstraintValidatorContext constraintValidatorContext) {
        String[] validTypes = {"checking", "savings", "usd", "business"};

        return transactionType != null
                && !transactionType.isEmpty()
                && Arrays.asList(validTypes).contains(transactionType.trim().toLowerCase());
    }
}