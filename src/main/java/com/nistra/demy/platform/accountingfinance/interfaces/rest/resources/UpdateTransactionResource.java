package com.nistra.demy.platform.accountingfinance.interfaces.rest.resources;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateTransactionResource(
        String transactionType,
        String transactionCategory,
        String transactionMethod,
        BigDecimal amount,
        String currency,
        String description,
        LocalDate transactionDate
) {
    public UpdateTransactionResource {
        if (transactionType == null || transactionType.isBlank())
            throw new IllegalArgumentException("Transaction type must be a non-null, non-blank value");
        if (transactionCategory == null || transactionCategory.isBlank())
            throw new IllegalArgumentException("Transaction category must be a non-null, non-blank value");
        if (transactionMethod == null || transactionMethod.isBlank())
            throw new IllegalArgumentException("Transaction method must be a non-null, non-blank value");
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Amount must be a positive, non-null value");
        if (currency == null || currency.isBlank())
            throw new IllegalArgumentException("Currency must be a non-null, non-blank value");
        if (transactionDate == null)
            throw new IllegalArgumentException("Transaction date must be a non-null value");
        if (transactionDate.isAfter(LocalDate.now()))
            throw new IllegalArgumentException("Transaction date cannot be in the future");
    }
}
