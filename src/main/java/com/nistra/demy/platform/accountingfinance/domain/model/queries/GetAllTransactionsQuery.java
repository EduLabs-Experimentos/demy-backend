package com.nistra.demy.platform.accountingfinance.domain.model.queries;

import com.nistra.demy.platform.accountingfinance.domain.model.valueobjects.TransactionCategory;
import com.nistra.demy.platform.accountingfinance.domain.model.valueobjects.TransactionMethod;
import com.nistra.demy.platform.accountingfinance.domain.model.valueobjects.TransactionType;

public record GetAllTransactionsQuery(
        TransactionCategory category,
        TransactionMethod method,
        TransactionType type
) {
}
