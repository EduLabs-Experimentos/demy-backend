package com.nistra.demy.platform.accountingfinance.infrastructure.reporting.pdf.aggregator;

import com.nistra.demy.platform.accountingfinance.domain.model.aggregates.Transaction;
import com.nistra.demy.platform.accountingfinance.domain.model.valueobjects.TransactionType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Aggregates transaction data for reporting purposes.
 * <p>
 * Provides methods to calculate totals grouped by currency and category,
 * supporting both all transactions and filtered by type.
 *
 * @author Salim Ramirez
 */
@Component
public class TransactionAggregator {

    /**
     * Calculates totals grouped by currency.
     *
     * @param transactions Transactions to process
     * @return Map of currency code to total amount
     */
    public Map<String, BigDecimal> calculateTotalsByCurrency(List<Transaction> transactions) {
        Map<String, BigDecimal> totals = new HashMap<>();
        transactions.forEach(t -> {
            String currency = t.getAmount().currency().toString();
            BigDecimal amount = t.getAmount().amount();
            totals.merge(currency, amount, BigDecimal::add);
        });
        return totals;
    }

    /**
     * Calculates totals grouped by category.
     *
     * @param transactions Transactions to process
     * @return Map of category name to total amount
     */
    public Map<String, BigDecimal> calculateTotalsByCategory(List<Transaction> transactions) {
        Map<String, BigDecimal> totals = new HashMap<>();
        transactions.forEach(t -> {
            String category = t.getTransactionCategory().name();
            BigDecimal amount = t.getAmount().amount();
            totals.merge(category, amount, BigDecimal::add);
        });
        return totals;
    }

    /**
     * Calculates expense totals grouped by category.
     *
     * @param transactions Transactions to process
     * @return Map of category name to total expenses
     */
    public Map<String, BigDecimal> calculateExpenseTotalsByCategory(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> TransactionType.EXPENSE.equals(t.getTransactionType()))
                .collect(Collectors.groupingBy(
                        t -> t.getTransactionCategory().name(),
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                t -> t.getAmount().amount(),
                                BigDecimal::add
                        )
                ));
    }
}

