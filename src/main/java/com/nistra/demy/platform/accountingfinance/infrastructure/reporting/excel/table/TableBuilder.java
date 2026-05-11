package com.nistra.demy.platform.accountingfinance.infrastructure.reporting.excel.table;

import com.nistra.demy.platform.accountingfinance.domain.model.aggregates.Transaction;
import com.nistra.demy.platform.accountingfinance.infrastructure.reporting.excel.table.summary.SummaryTableBuilder;
import com.nistra.demy.platform.accountingfinance.infrastructure.reporting.excel.table.header.HeaderBuilder;
import com.nistra.demy.platform.accountingfinance.infrastructure.reporting.excel.table.row.RowBuilder;
import com.nistra.demy.platform.shared.domain.model.valueobjects.Money;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;

import java.util.Currency;
import java.util.List;
import java.util.Map;

/**
 * Facade for Excel table building operations.
 * <p>
 * Coordinates header, row, and summary builders to provide
 * a unified interface for creating Excel tables.
 *
 * @author Salim Ramirez
 */
@Component
public class TableBuilder {

    private final HeaderBuilder headerBuilder;
    private final RowBuilder rowBuilder;
    private final SummaryTableBuilder summaryBuilder;

    public TableBuilder(
            HeaderBuilder headerBuilder,
            RowBuilder rowBuilder,
            SummaryTableBuilder summaryBuilder
    ) {
        this.headerBuilder = headerBuilder;
        this.rowBuilder = rowBuilder;
        this.summaryBuilder = summaryBuilder;
    }

    /**
     * Adds header row to sheet.
     *
     * @param sheet Sheet to add headers to
     * @param headerStyle Style for header cells
     */
    public void addHeaders(Sheet sheet, CellStyle headerStyle) {
        headerBuilder.addHeaders(sheet, headerStyle);
    }

    /**
     * Adds transaction rows to sheet.
     *
     * @param sheet Sheet to add rows to
     * @param transactions Transactions to add
     * @param rowMapper Mapper to convert transaction to row
     * @return Index of next available row
     */
    public int addTransactionRows(
            Sheet sheet,
            List<Transaction> transactions,
            RowBuilder.TransactionRowMapper rowMapper
    ) {
        return rowBuilder.addTransactionRows(sheet, transactions, rowMapper);
    }

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
    public int addTotalsByCurrencyTable(
            Sheet sheet,
            int startRowIdx,
            Map<Currency, Money> totalsByCurrency,
            CellStyle headerStyle,
            Map<Currency, CellStyle> currencyStyles
    ) {
        return summaryBuilder.addTotalsTable(sheet, startRowIdx, totalsByCurrency, headerStyle, currencyStyles);
    }

    /**
     * Gets the number of header columns.
     *
     * @return Header count
     */
    public int getHeaderCount() {
        return headerBuilder.getHeaderCount();
    }
}

