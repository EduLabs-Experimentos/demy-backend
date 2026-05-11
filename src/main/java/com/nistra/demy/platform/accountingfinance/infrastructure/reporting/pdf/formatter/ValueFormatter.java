package com.nistra.demy.platform.accountingfinance.infrastructure.reporting.pdf.formatter;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Formats values for PDF report presentation.
 * <p>
 * Provides utility methods to format amounts and dates
 * in a consistent, human-readable format.
 *
 * @author Salim Ramirez
 */
@Component
public class ValueFormatter {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Formats a monetary amount with currency.
     *
     * @param amount Amount to format
     * @param currency Currency code (e.g., "USD", "EUR")
     * @return Formatted string (e.g., "1,234.56 USD")
     */
    public String formatAmount(BigDecimal amount, String currency) {
        return String.format("%,.2f %s", amount, currency);
    }

    /**
     * Formats a date for report display.
     *
     * @param date Date to format
     * @return Date string in yyyy-MM-dd format
     */
    public String formatDate(LocalDate date) {
        return date.format(DATE_FORMATTER);
    }
}

