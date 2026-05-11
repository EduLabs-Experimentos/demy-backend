package com.nistra.demy.platform.accountingfinance.infrastructure.reporting.excel.table.summary;

import com.nistra.demy.platform.shared.domain.model.valueobjects.Money;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.Currency;
import java.util.Map;

/**
 * Builds currency summary tables for Excel reports.
 * <p>
 * Creates formatted tables showing totals grouped by currency
 * with appropriate styling.
 *
 * @author Salim Ramirez
 */
@Component
public class SummaryTableBuilder {

    /**
     * Adds currency totals table to sheet.
     *
     * @param sheet Sheet to add table to
     * @param startRowIdx Starting row index
     * @param totalsByCurrency Map of currency totals
     * @param headerStyle Style for header cells
     * @param currencyStyles Styles per currency
     * @return Index of next available row
     */
    public int addTotalsTable(
            Sheet sheet,
            int startRowIdx,
            Map<Currency, Money> totalsByCurrency,
            CellStyle headerStyle,
            Map<Currency, CellStyle> currencyStyles
    ) {
        int currentRow = startRowIdx + 1;

        currentRow = addTableTitle(sheet, currentRow, headerStyle);
        currentRow = addTableHeaders(sheet, currentRow, headerStyle);
        currentRow = addTableData(sheet, currentRow, totalsByCurrency, currencyStyles);

        return currentRow;
    }

    private int addTableTitle(Sheet sheet, int rowIdx, CellStyle headerStyle) {
        Row titleRow = sheet.createRow(rowIdx++);
        Cell titleCell = titleRow.createCell(3);
        titleCell.setCellValue("Totales por Moneda:");
        titleCell.setCellStyle(headerStyle);
        return rowIdx;
    }

    private int addTableHeaders(Sheet sheet, int rowIdx, CellStyle headerStyle) {
        Row headerRow = sheet.createRow(rowIdx++);

        Cell currencyHeaderCell = headerRow.createCell(3);
        currencyHeaderCell.setCellValue("Moneda");
        currencyHeaderCell.setCellStyle(headerStyle);

        Cell amountHeaderCell = headerRow.createCell(4);
        amountHeaderCell.setCellValue("Total");
        amountHeaderCell.setCellStyle(headerStyle);

        return rowIdx;
    }

    private int addTableData(
            Sheet sheet,
            int startRow,
            Map<Currency, Money> totalsByCurrency,
            Map<Currency, CellStyle> currencyStyles
    ) {
        final int[] rowCounter = {startRow};

        totalsByCurrency.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(Comparator.comparing(Currency::getCurrencyCode)))
                .forEach(entry -> {
                    Currency currency = entry.getKey();
                    Money total = entry.getValue();

                    Row totalRow = sheet.createRow(rowCounter[0]++);

                    Cell currencyCell = totalRow.createCell(3);
                    currencyCell.setCellValue(currency.getCurrencyCode() + " - " + currency.getDisplayName());

                    Cell totalCell = totalRow.createCell(4);
                    totalCell.setCellValue(total.amount().doubleValue());
                    totalCell.setCellStyle(currencyStyles.get(currency));
                });

        return rowCounter[0];
    }
}

