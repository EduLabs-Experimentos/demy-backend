package com.nistra.demy.platform.accountingfinance.domain.model.valueobjects;

import com.nistra.demy.platform.accountingfinance.domain.exceptions.InvalidTransactionTypeException;

public enum TransactionType {
    INCOME,
    EXPENSE;

    public static TransactionType fromString(String type) {
        if (type == null || type.isBlank())
            throw new IllegalArgumentException("Transaction type cannot be null or blank");

        String normalized = type.trim()
                .replace("-", "_")
                .replace(" ", "_")
                .toUpperCase();

        for (TransactionType tt : TransactionType.values()) {
            if (tt.name().equalsIgnoreCase(normalized)) {
                return tt;
            }
        }
        throw new InvalidTransactionTypeException(type);
    }
}
