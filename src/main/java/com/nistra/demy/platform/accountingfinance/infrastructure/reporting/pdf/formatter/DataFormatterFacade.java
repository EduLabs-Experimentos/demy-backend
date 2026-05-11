package com.nistra.demy.platform.accountingfinance.infrastructure.reporting.pdf.formatter;

import com.nistra.demy.platform.accountingfinance.domain.model.aggregates.Transaction;
import com.nistra.demy.platform.accountingfinance.infrastructure.reporting.pdf.aggregator.TransactionAggregator;
import com.nistra.demy.platform.accountingfinance.infrastructure.reporting.pdf.aggregator.TimeSeriesCalculator;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Facade for transaction data formatting operations.
 * <p>
 * Coordinates aggregators and formatters to provide
 * a unified interface for PDF report data transformation.
 *
 * @author Salim Ramirez
 */
@Component
public class DataFormatterFacade {

    private final TransactionAggregator aggregator;
    private final TimeSeriesCalculator timeSeriesCalculator;
    private final ValueFormatter valueFormatter;

    public DataFormatterFacade(
            TransactionAggregator aggregator,
            TimeSeriesCalculator timeSeriesCalculator,
            ValueFormatter valueFormatter
    ) {
        this.aggregator = aggregator;
        this.timeSeriesCalculator = timeSeriesCalculator;
        this.valueFormatter = valueFormatter;
    }

    /**
     * Calculates totals grouped by currency.
     *
     * @param transactions Transactions to process
     * @return Map of currency code to total amount
     */
    public Map<String, BigDecimal> calculateTotalsByCurrency(List<Transaction> transactions) {
        return aggregator.calculateTotalsByCurrency(transactions);
    }

    /**
     * Calculates totals grouped by category.
     *
     * @param transactions Transactions to process
     * @return Map of category name to total amount
     */
    public Map<String, BigDecimal> calculateTotalsByCategory(List<Transaction> transactions) {
        return aggregator.calculateTotalsByCategory(transactions);
    }

    /**
     * Calculates expense totals grouped by category.
     *
     * @param transactions Transactions to process
     * @return Map of category name to total expenses
     */
    public Map<String, BigDecimal> calculateExpenseTotalsByCategory(List<Transaction> transactions) {
        return aggregator.calculateExpenseTotalsByCategory(transactions);
    }

    /**
     * Calculates income totals grouped by date.
     *
     * @param transactions Transactions to process
     * @return Map of date string to total income
     */
    public Map<String, BigDecimal> calculateIncomeTotalsOverTime(List<Transaction> transactions) {
        return timeSeriesCalculator.calculateIncomeTotalsOverTime(transactions);
    }

    /**
     * Calculates all transaction totals grouped by date.
     *
     * @param transactions Transactions to process
     * @return Map of date string to total amount
     */
    public Map<String, BigDecimal> calculateTotalsOverTime(List<Transaction> transactions) {
        return timeSeriesCalculator.calculateTotalsOverTime(transactions);
    }

    /**
     * Formats a monetary amount with currency.
     *
     * @param amount Amount to format
     * @param currency Currency code
     * @return Formatted string
     */
    public String formatAmount(BigDecimal amount, String currency) {
        return valueFormatter.formatAmount(amount, currency);
    }

    /**
     * Formats a date for report display.
     *
     * @param date Date to format
     * @return Formatted date string
     */
    public String formatDate(LocalDate date) {
        return valueFormatter.formatDate(date);
    }
}

