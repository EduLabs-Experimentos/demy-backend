package com.nistra.demy.platform.accountingfinance.infrastructure.reporting.excel.formatter;

import com.nistra.demy.platform.accountingfinance.domain.model.aggregates.Transaction;
import com.nistra.demy.platform.shared.domain.model.valueobjects.Money;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Formats transaction data for Excel reports.
 * <p>
 * Provides utility methods to format dates, amounts, and descriptions,
 * and calculate totals by currency for Excel export.
 *
 * @author Salim Ramirez
 */
@Component
public class DataFormatter {

    /**
     * Date formatter for Excel cells (dd/MM/yyyy format).
     */
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Formats a date for Excel display.
     *
     * @param date Date to format
     * @return Formatted date string (dd/MM/yyyy)
     */
    public String formatDate(LocalDate date) {
        return date.format(DATE_FORMATTER);
    }

    /**
     * Extracts numeric value from Money object.
     *
     * @param money Money object
     * @return Amount as double value
     */
    public double getAmountValue(Money money) {
        return money.amount().doubleValue();
    }

    /**
     * Formats description text, replacing null with dash.
     *
     * @param description Transaction description
     * @return Formatted description or "-" if null
     */
    public String formatDescription(String description) {
        return description != null ? description : "-";
    }

    /**
     * Calculates totals grouped by currency.
     *
     * @param transactions Transactions to process
     * @return Map of currency to total amount
     */
    public Map<Currency, Money> calculateTotalsByCurrency(List<Transaction> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            return Collections.emptyMap();
        }

        return transactions.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getAmount().currency(),
                        Collectors.reducing(
                                null,
                                Transaction::getAmount,
                                (m1, m2) -> {
                                    if (m1 == null) return m2;
                                    if (m2 == null) return m1;
                                    return m1.add(m2);
                                }
                        )
                ));
    }

    /**
     * Gets unique currencies from transactions.
     *
     * @param transactions Transactions to process
     * @return Sorted set of unique currencies
     */
    public Set<Currency> getUniqueCurrencies(List<Transaction> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            return Collections.emptySet();
        }

        return transactions.stream()
                .map(t -> t.getAmount().currency())
                .collect(Collectors.toCollection(() ->
                    new TreeSet<>(Comparator.comparing(Currency::getCurrencyCode))
                ));
    }
}

