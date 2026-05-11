package com.nistra.demy.platform.accountingfinance.infrastructure.reporting.excel.workbook.style;

import org.springframework.stereotype.Component;

import java.util.Currency;

/**
 * Provides Excel accounting format strings for currencies.
 * <p>
 * Generates locale-specific currency format strings
 * for proper display in Excel cells.
 *
 * @author Salim Ramirez
 */
@Component
public class CurrencyFormatProvider {

    /**
     * Gets accounting format string for currency.
     *
     * @param currency Currency to format
     * @return Excel format string
     */
    public String getAccountingFormatString(Currency currency) {
        return switch (currency.getCurrencyCode()) {
            case "USD" -> "_-$* #,##0.00_-;-$* #,##0.00_-;_-$* \"-\"??_-;_-@_-";
            case "PEN" -> "_-\"S/\"* #,##0.00_-;-\"S/\"* #,##0.00_-;_-\"S/\"* \"-\"??_-;_-@_-";
            case "EUR" -> "_-[$€-407]* #,##0.00_-;-[$€-407]* #,##0.00_-;_-[$€-407]* \"-\"??_-;_-@_-";
            default -> "_-\"" + currency.getSymbol() + "\"* #,##0.00_-;-\"" + currency.getSymbol() +
                       "\"* #,##0.00_-;_-\"" + currency.getSymbol() + "\"* \"-\"??_-;_-@_-";
        };
    }
}

