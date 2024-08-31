package com.financialhub.app.validations.transaction;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = TransactionValidator.class)
public @interface ValidTransactionType {
    public String message() default "Invalid type, it should be either: Deposit, Withdrawal, Transfer, Payment or Collection";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}