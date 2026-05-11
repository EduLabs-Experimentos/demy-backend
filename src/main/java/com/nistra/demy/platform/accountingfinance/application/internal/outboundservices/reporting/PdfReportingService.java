package com.nistra.demy.platform.accountingfinance.application.internal.outboundservices.reporting;

import com.nistra.demy.platform.accountingfinance.domain.model.aggregates.Transaction;

import java.util.List;

/**
 * Outbound service for PDF report generation.
 * <p>
 * Defines the contract for generating PDF reports from transaction data.
 * Implementations handle the specific PDF library and formatting logic.
 *
 * @author Salim Ramirez
 */
public interface PdfReportingService {

    /**
     * Generates a PDF report from a list of transactions.
     *
     * @param transactions List of transactions to include in report
     * @return PDF file content as byte array
     */
    byte[] generateTransactionsPdfReport(List<Transaction> transactions);
}
