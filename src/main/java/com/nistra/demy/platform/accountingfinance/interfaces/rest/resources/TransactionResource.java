package com.nistra.demy.platform.accountingfinance.interfaces.rest.resources;

import java.math.BigDecimal;

public record TransactionResource(
        Long id,
        String transactionType,
        String transactionCategory,
        String transactionMethod,
        BigDecimal amount,
        String currency,
        String description,
        String transactionDate
) {
}
