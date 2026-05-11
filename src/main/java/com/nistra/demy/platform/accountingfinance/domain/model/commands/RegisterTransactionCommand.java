package com.nistra.demy.platform.accountingfinance.domain.model.commands;

import com.nistra.demy.platform.shared.domain.model.valueobjects.Money;

import java.time.LocalDate;

public record RegisterTransactionCommand(
        String transactionType,
        String transactionCategory,
        String transactionMethod,
        Money amount,
        String description,
        LocalDate transactionDate
) {
    public RegisterTransactionCommand {
        if (transactionType == null || transactionType.isBlank())
            throw new IllegalArgumentException("Transaction type cannot be null or blank");
        if (transactionCategory == null || transactionCategory.isBlank())
            throw new IllegalArgumentException("Transaction category cannot be null or blank");
        if (transactionMethod == null || transactionMethod.isBlank())
            throw new IllegalArgumentException("Transaction method cannot be null or blank");
        if (amount == null)
            throw new IllegalArgumentException("Amount cannot be null");
        if (description == null || description.isBlank())
            throw new IllegalArgumentException("Description cannot be null or blank");
        if (transactionDate == null)
            throw new IllegalArgumentException("Transaction date cannot be null");
    }
}
