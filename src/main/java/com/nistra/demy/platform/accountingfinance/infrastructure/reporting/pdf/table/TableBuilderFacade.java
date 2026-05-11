package com.nistra.demy.platform.accountingfinance.infrastructure.reporting.pdf.table;

import com.itextpdf.layout.element.Table;
import com.nistra.demy.platform.accountingfinance.domain.model.aggregates.Transaction;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Facade for PDF table building operations.
 * <p>
 * Coordinates specialized table builders to provide a unified
 * interface for creating tables in PDF reports.
 *
 * @author Salim Ramirez
 */
@Component
public class TableBuilderFacade {

    private final TransactionDetailTableBuilder transactionDetailTableBuilder;
    private final CurrencySummaryTableBuilder currencySummaryTableBuilder;

    public TableBuilderFacade(
            TransactionDetailTableBuilder transactionDetailTableBuilder,
            CurrencySummaryTableBuilder currencySummaryTableBuilder
    ) {
        this.transactionDetailTableBuilder = transactionDetailTableBuilder;
        this.currencySummaryTableBuilder = currencySummaryTableBuilder;
    }

    /**
     * Builds a table with transaction details.
     *
     * @param transactions List of transactions to display
     * @return Formatted table with transaction data
     */
    public Table buildTransactionsTable(List<Transaction> transactions) {
        return transactionDetailTableBuilder.buildTable(transactions);
    }

    /**
     * Builds a summary table with currency totals.
     *
     * @param totalsByCurrency Map of currency codes to totals
     * @return Formatted summary table
     */
    public Table buildCurrencySummaryTable(Map<String, BigDecimal> totalsByCurrency) {
        return currencySummaryTableBuilder.buildCurrencySummaryTable(totalsByCurrency);
    }
}

