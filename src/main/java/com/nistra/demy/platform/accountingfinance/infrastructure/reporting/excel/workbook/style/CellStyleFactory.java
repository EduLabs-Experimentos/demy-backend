package com.nistra.demy.platform.accountingfinance.infrastructure.reporting.excel.workbook.style;

import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;

import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

/**
 * Factory for creating Excel cell styles.
 * <p>
 * Provides methods to create various cell styles including
 * headers, text, numbers, and currency formats with caching.
 *
 * @author Salim Ramirez
 */
@Component
public class CellStyleFactory {

    private final CurrencyFormatProvider formatProvider;
    private final Map<String, CellStyle> currencyStyleCache = new HashMap<>();

    public CellStyleFactory(CurrencyFormatProvider formatProvider) {
        this.formatProvider = formatProvider;
    }

    /**
     * Creates header cell style.
     *
     * @param workbook Workbook to create style in
     * @return Header style (bold, centered)
     */
    public CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        return headerStyle;
    }

    /**
     * Creates text cell style.
     *
     * @param workbook Workbook to create style in
     * @return Text style (left aligned)
     */
    public CellStyle createTextStyle(Workbook workbook) {
        CellStyle textStyle = workbook.createCellStyle();
        textStyle.setAlignment(HorizontalAlignment.LEFT);
        return textStyle;
    }

    /**
     * Creates number cell style.
     *
     * @param workbook Workbook to create style in
     * @return Number style (right aligned)
     */
    public CellStyle createNumberStyle(Workbook workbook) {
        CellStyle numberStyle = workbook.createCellStyle();
        numberStyle.setAlignment(HorizontalAlignment.RIGHT);
        return numberStyle;
    }

    /**
     * Creates currency cell style with caching.
     *
     * @param workbook Workbook to create style in
     * @param currency Currency for formatting
     * @return Currency style (right aligned, formatted)
     */
    public CellStyle createCurrencyStyle(Workbook workbook, Currency currency) {
        String cacheKey = workbook.hashCode() + "_" + currency.getCurrencyCode();

        if (currencyStyleCache.containsKey(cacheKey)) {
            return currencyStyleCache.get(cacheKey);
        }

        CellStyle currencyStyle = workbook.createCellStyle();
        currencyStyle.setAlignment(HorizontalAlignment.RIGHT);
        applyCurrencyFormat(currencyStyle, workbook, currency);

        currencyStyleCache.put(cacheKey, currencyStyle);
        return currencyStyle;
    }

    /**
     * Creates bold currency cell style.
     *
     * @param workbook Workbook to create style in
     * @param currency Currency for formatting
     * @return Bold currency style
     */
    public CellStyle createBoldCurrencyStyle(Workbook workbook, Currency currency) {
        CellStyle boldCurrencyStyle = workbook.createCellStyle();
        boldCurrencyStyle.setAlignment(HorizontalAlignment.RIGHT);

        Font boldFont = workbook.createFont();
        boldFont.setBold(true);
        boldCurrencyStyle.setFont(boldFont);

        applyCurrencyFormat(boldCurrencyStyle, workbook, currency);

        return boldCurrencyStyle;
    }

    /**
     * Clears the currency style cache.
     */
    public void clearCache() {
        currencyStyleCache.clear();
    }

    private void applyCurrencyFormat(CellStyle style, Workbook workbook, Currency currency) {
        DataFormat format = workbook.createDataFormat();
        String formatString = formatProvider.getAccountingFormatString(currency);
        style.setDataFormat(format.getFormat(formatString));
    }
}

