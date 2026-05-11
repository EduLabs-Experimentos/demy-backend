package com.nistra.demy.platform.accountingfinance.domain.services;

import com.nistra.demy.platform.accountingfinance.domain.model.valueobjects.TransactionCategory;
import com.nistra.demy.platform.accountingfinance.domain.model.valueobjects.TransactionMethod;
import com.nistra.demy.platform.accountingfinance.domain.model.valueobjects.TransactionType;

public interface ReportCommandService {
    byte[] generateTransactionsPdfReport(TransactionCategory category, TransactionMethod method, TransactionType type);

    byte[] generateTransactionsExcelReport(TransactionCategory category, TransactionMethod method, TransactionType type);
}
