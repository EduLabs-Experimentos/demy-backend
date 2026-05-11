package com.nistra.demy.platform.accountingfinance.infrastructure.reporting.pdf.aggregator;

import com.nistra.demy.platform.accountingfinance.domain.model.aggregates.Transaction;
import com.nistra.demy.platform.accountingfinance.domain.model.valueobjects.TransactionType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Calculates transaction time series data for reports.
 * <p>
 * Provides methods to group transactions by date, supporting
 * both income-only and all transaction types.
 *
 * @author Salim Ramirez
 */
@Component
public class TimeSeriesCalculator {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Calculates income totals grouped by date.
     *
     * @param transactions Transactions to process
     * @return Map of date string to total income
     */
    public Map<String, BigDecimal> calculateIncomeTotalsOverTime(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> TransactionType.INCOME.equals(t.getTransactionType()))
                .collect(Collectors.groupingBy(
                        t -> t.getTransactionDate().format(DATE_FORMATTER),
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                t -> t.getAmount().amount(),
                                BigDecimal::add
                        )
                ));
    }

    /**
     * Calculates all transaction totals grouped by date.
     *
     * @param transactions Transactions to process
     * @return Map of date string to total amount
     */
    public Map<String, BigDecimal> calculateTotalsOverTime(List<Transaction> transactions) {
        Map<String, BigDecimal> totals = new HashMap<>();
        transactions.forEach(t -> {
            String dateKey = t.getTransactionDate().format(DATE_FORMATTER);
            BigDecimal amount = t.getAmount().amount();
            totals.merge(dateKey, amount, BigDecimal::add);
        });
        return totals;
    }
}

