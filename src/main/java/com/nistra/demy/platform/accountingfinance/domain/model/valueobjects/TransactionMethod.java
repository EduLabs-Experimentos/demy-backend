package com.nistra.demy.platform.accountingfinance.domain.model.valueobjects;

import com.nistra.demy.platform.accountingfinance.domain.exceptions.InvalidTransactionMethodException;

public enum TransactionMethod {
    CASH,
    CREDIT_CARD,
    DEBIT_CARD,
    BANK_TRANSFER,
    WALLET,
    OTHER;

    public static TransactionMethod fromString(String method) {
        if (method == null || method.isBlank())
            throw new IllegalArgumentException("Transaction method cannot be null or blank");

        String normalized = method.trim()
                .replace("-", "_")
                .replace(" ", "_")
                .toUpperCase();

        for (TransactionMethod tm : TransactionMethod.values()) {
            if (tm.name().equalsIgnoreCase(normalized)) {
                return tm;
            }
        }
        throw new InvalidTransactionMethodException(method);
    }
}
