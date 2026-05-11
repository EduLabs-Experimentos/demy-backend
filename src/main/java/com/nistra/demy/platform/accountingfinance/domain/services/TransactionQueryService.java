package com.nistra.demy.platform.accountingfinance.domain.services;

import com.nistra.demy.platform.accountingfinance.domain.model.aggregates.Transaction;
import com.nistra.demy.platform.accountingfinance.domain.model.queries.GetAllTransactionsQuery;
import com.nistra.demy.platform.accountingfinance.domain.model.queries.GetTransactionByIdQuery;

import java.util.List;
import java.util.Optional;

public interface TransactionQueryService {
    Optional<Transaction> handle(GetTransactionByIdQuery query);

    List<Transaction> handle(GetAllTransactionsQuery query);
}
