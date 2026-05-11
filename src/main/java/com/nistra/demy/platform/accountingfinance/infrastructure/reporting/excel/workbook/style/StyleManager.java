package com.nistra.demy.platform.accountingfinance.infrastructure.reporting.excel.workbook.style;

import com.nistra.demy.platform.accountingfinance.infrastructure.reporting.excel.workbook.WorkbookBuilder;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;

import java.util.Currency;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Manages Excel cell styles for transactions.
 * <p>
 * Creates and caches header styles, currency styles,
 * and bold currency styles for Excel reports.
 *
 * @author Salim Ramirez
 */
@Component
public class StyleManager {

    private final WorkbookBuilder workbookBuilder;

    public StyleManager(WorkbookBuilder workbookBuilder) {
        this.workbookBuilder = workbookBuilder;
    }

    /**
     * Creates header style for workbook.
     *
     * @param workbook Target workbook
     * @return Header cell style
     */
    public CellStyle createHeaderStyle(Workbook workbook) {
        return workbookBuilder.createHeaderStyle(workbook);
    }

    /**
     * Creates currency styles for all currencies.
     *
     * @param workbook Target workbook
     * @param currencies Set of currencies to create styles for
     * @return Map of currency to cell style
     */
    public Map<Currency, CellStyle> createCurrencyStyles(Workbook workbook, Set<Currency> currencies) {
        Map<Currency, CellStyle> styles = new HashMap<>();
        for (Currency currency : currencies) {
            styles.put(currency, workbookBuilder.createCurrencyStyle(workbook, currency));
        }
        return styles;
    }

    /**
     * Creates bold currency styles for all currencies.
     *
     * @param workbook Target workbook
     * @param currencies Set of currencies to create styles for
     * @return Map of currency to bold cell style
     */
    public Map<Currency, CellStyle> createBoldCurrencyStyles(Workbook workbook, Set<Currency> currencies) {
        Map<Currency, CellStyle> styles = new HashMap<>();
        for (Currency currency : currencies) {
            styles.put(currency, workbookBuilder.createBoldCurrencyStyle(workbook, currency));
        }
        return styles;
    }
}

