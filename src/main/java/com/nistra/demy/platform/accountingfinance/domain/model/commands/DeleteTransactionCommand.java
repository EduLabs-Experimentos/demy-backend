package com.nistra.demy.platform.accountingfinance.domain.model.commands;

public record DeleteTransactionCommand(Long transactionId) {
    public DeleteTransactionCommand {
        if (transactionId == null || transactionId <= 0)
            throw new IllegalArgumentException("Transaction ID must be a positive, non-null value");
    }
}

