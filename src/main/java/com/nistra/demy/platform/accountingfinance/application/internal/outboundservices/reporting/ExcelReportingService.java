package com.nistra.demy.platform.accountingfinance.application.internal.outboundservices.reporting;

import com.nistra.demy.platform.accountingfinance.domain.model.aggregates.Transaction;

import java.util.List;

/**
 * Outbound service for Excel report generation.
 * <p>
 * Defines the contract for generating Excel reports from transaction data.
 * Implementations handle the specific Excel library and formatting logic.
 *
 * @author Salim Ramirez
 */
public interface ExcelReportingService {

    /**
     * Generates an Excel report from a list of transactions.
     *
     * @param transactions List of transactions to include in report
     * @return Excel file content as byte array
     */
    byte[] generateTransactionsExcelReport(List<Transaction> transactions);
}
