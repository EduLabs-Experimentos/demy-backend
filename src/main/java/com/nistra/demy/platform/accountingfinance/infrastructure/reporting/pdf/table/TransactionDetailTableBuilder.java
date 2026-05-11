package com.nistra.demy.platform.accountingfinance.infrastructure.reporting.pdf.table;

import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.nistra.demy.platform.accountingfinance.domain.model.aggregates.Transaction;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Builds transaction detail tables for PDF reports.
 * <p>
 * Creates formatted tables displaying transaction data
 * with headers and styled cells.
 *
 * @author Salim Ramirez
 */
@Component
public class TransactionDetailTableBuilder {

    private static final float[] COLUMN_WIDTHS = {70f, 60f, 70f, 65f, 80f, 120f};

    private final CellStyler cellStyler;

    public TransactionDetailTableBuilder(CellStyler cellStyler) {
        this.cellStyler = cellStyler;
    }

    /**
     * Builds a table with transaction details.
     *
     * @param transactions List of transactions to display
     * @return Formatted table with transaction data
     */
    public Table buildTable(List<Transaction> transactions) {
        Table table = createTableStructure();
        addHeaders(table);
        addDataRows(table, transactions);
        return table;
    }

    private Table createTableStructure() {
        Table table = new Table(UnitValue.createPointArray(COLUMN_WIDTHS));
        table.setWidth(UnitValue.createPercentValue(100));
        table.setKeepTogether(false);
        return table;
    }

    private void addHeaders(Table table) {
        table.addHeaderCell(cellStyler.createTransactionHeaderCell("Fecha"));
        table.addHeaderCell(cellStyler.createTransactionHeaderCell("Tipo"));
        table.addHeaderCell(cellStyler.createTransactionHeaderCell("Categoría"));
        table.addHeaderCell(cellStyler.createTransactionHeaderCell("Método"));
        table.addHeaderCell(cellStyler.createTransactionHeaderCell("Monto"));
        table.addHeaderCell(cellStyler.createTransactionHeaderCell("Descripción"));
    }

    private void addDataRows(Table table, List<Transaction> transactions) {
        transactions.forEach(transaction -> {
            String currency = transaction.getAmount().currency().toString();
            String formattedAmount = String.format("%,.2f %s",
                    transaction.getAmount().amount(), currency);

            table.addCell(cellStyler.createTransactionDataCell(
                    transaction.getTransactionDate().toString(), TextAlignment.LEFT));
            table.addCell(cellStyler.createTransactionDataCell(
                    transaction.getTransactionType().toString(), TextAlignment.LEFT));
            table.addCell(cellStyler.createTransactionDataCell(
                    transaction.getTransactionCategory().toString(), TextAlignment.LEFT));
            table.addCell(cellStyler.createTransactionDataCell(
                    transaction.getTransactionMethod().toString(), TextAlignment.LEFT));
            table.addCell(cellStyler.createTransactionDataCell(
                    formattedAmount, TextAlignment.RIGHT));
            table.addCell(cellStyler.createTransactionDataCell(
                    transaction.getDescription() != null ? transaction.getDescription() : "-",
                    TextAlignment.LEFT));
        });
    }
}

