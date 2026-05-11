package com.nistra.demy.platform.accountingfinance.infrastructure.reporting.excel.table.row;

import com.nistra.demy.platform.accountingfinance.domain.model.aggregates.Transaction;
import com.nistra.demy.platform.accountingfinance.infrastructure.reporting.excel.formatter.DataFormatter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Component;

/**
 * Maps transaction data to Excel rows.
 * <p>
 * Handles the conversion of transaction domain objects
 * to Excel row format with appropriate cell values and styles.
 *
 * @author Salim Ramirez
 */
@Component
public class TransactionRowMapper {

    private final DataFormatter dataFormatter;

    public TransactionRowMapper(DataFormatter dataFormatter) {
        this.dataFormatter = dataFormatter;
    }

    /**
     * Maps a transaction to an Excel row.
     *
     * @param transaction Transaction to map
     * @param row Excel row to populate
     * @param currencyStyle Style for currency cell
     */
    public void mapToRow(Transaction transaction, Row row, CellStyle currencyStyle) {
        row.createCell(0).setCellValue(
                dataFormatter.formatDate(transaction.getTransactionDate())
        );

        row.createCell(1).setCellValue(
                transaction.getTransactionType().toString()
        );

        row.createCell(2).setCellValue(
                transaction.getTransactionCategory().toString()
        );

        row.createCell(3).setCellValue(
                transaction.getTransactionMethod().toString()
        );

        Cell amountCell = row.createCell(4);
        amountCell.setCellValue(
                dataFormatter.getAmountValue(transaction.getAmount())
        );
        amountCell.setCellStyle(currencyStyle);

        row.createCell(5).setCellValue(
                dataFormatter.formatDescription(transaction.getDescription())
        );
    }
}

