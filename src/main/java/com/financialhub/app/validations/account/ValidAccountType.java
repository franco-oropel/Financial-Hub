package com.financialhub.app.validations.account;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = AccountValidator.class)
public @interface ValidAccountType {
    public String message() default "Invalid type, it should be either: Checking, Savings, USD or Business";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}