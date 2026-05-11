package com.nistra.demy.platform.accountingfinance.domain.model.queries;

public record GetTransactionByIdQuery(Long transactionId) {
    public GetTransactionByIdQuery {
        if (transactionId == null || transactionId <= 0)
            throw new IllegalArgumentException("Transaction ID must be a positive, non-null value");
    }
}

