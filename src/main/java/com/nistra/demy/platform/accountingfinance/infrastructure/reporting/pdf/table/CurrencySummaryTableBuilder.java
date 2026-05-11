package com.nistra.demy.platform.accountingfinance.infrastructure.reporting.pdf.table;

import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Builds summary tables for PDF reports.
 * <p>
 * Creates formatted tables displaying currency totals
 * and other summary information.
 *
 * @author Salim Ramirez
 */
@Component
public class CurrencySummaryTableBuilder {

    private static final float[] COLUMN_WIDTHS = {120f, 120f};

    private final CellStyler cellStyler;

    public CurrencySummaryTableBuilder(CellStyler cellStyler) {
        this.cellStyler = cellStyler;
    }

    /**
     * Builds a summary table with currency totals.
     *
     * @param totalsByCurrency Map of currency codes to totals
     * @return Formatted summary table
     */
    public Table buildCurrencySummaryTable(Map<String, BigDecimal> totalsByCurrency) {
        Table table = createTableStructure();
        addHeaders(table);
        addDataRows(table, totalsByCurrency);
        return table;
    }

    private Table createTableStructure() {
        Table table = new Table(UnitValue.createPointArray(COLUMN_WIDTHS));
        table.setWidth(UnitValue.createPercentValue(35));
        table.setMarginTop(10);
        table.setMarginBottom(10);
        return table;
    }

    private void addHeaders(Table table) {
        table.addHeaderCell(cellStyler.createSummaryHeaderCell("Moneda"));
        table.addHeaderCell(cellStyler.createSummaryHeaderCell("Total"));
    }

    private void addDataRows(Table table, Map<String, BigDecimal> totalsByCurrency) {
        totalsByCurrency.forEach((currency, total) -> {
            table.addCell(cellStyler.createSummaryDataCell(currency, TextAlignment.LEFT));
            table.addCell(cellStyler.createSummaryDataCell(
                    String.format("%,.2f", total), TextAlignment.RIGHT));
        });
    }
}

