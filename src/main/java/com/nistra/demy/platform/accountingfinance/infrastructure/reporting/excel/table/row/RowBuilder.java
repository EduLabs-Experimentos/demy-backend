package com.nistra.demy.platform.accountingfinance.infrastructure.reporting.excel.table.row;

import com.nistra.demy.platform.accountingfinance.domain.model.aggregates.Transaction;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Builds data rows for Excel transaction tables.
 * <p>
 * Handles the creation of transaction detail rows
 * using a provided mapping strategy.
 *
 * @author Salim Ramirez
 */
@Component
public class RowBuilder {

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
            TransactionRowMapper rowMapper
    ) {
        int rowIdx = 1;
        for (Transaction transaction : transactions) {
            Row row = sheet.createRow(rowIdx++);
            rowMapper.mapTransactionToRow(transaction, row);
        }
        return rowIdx;
    }

    /**
     * Functional interface for mapping transactions to Excel rows.
     */
    @FunctionalInterface
    public interface TransactionRowMapper {
        /**
         * Maps a transaction to an Excel row.
         *
         * @param transaction Transaction to map
         * @param row Excel row to populate
         */
        void mapTransactionToRow(Transaction transaction, Row row);
    }
}

