package com.nistra.demy.platform.accountingfinance.infrastructure.reporting.pdf.section;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import com.nistra.demy.platform.accountingfinance.domain.model.aggregates.Transaction;
import com.nistra.demy.platform.accountingfinance.infrastructure.reporting.pdf.document.DocumentBuilder;
import com.nistra.demy.platform.accountingfinance.infrastructure.reporting.pdf.formatter.DataFormatterFacade;
import com.nistra.demy.platform.accountingfinance.infrastructure.reporting.pdf.table.TableBuilderFacade;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Builds table sections for PDF reports.
 * <p>
 * Handles the creation of transaction tables and
 * summary sections with totals by currency.
 *
 * @author Salim Ramirez
 */
@Component
public class TableSectionBuilder {

    private final DocumentBuilder documentBuilder;
    private final TableBuilderFacade tableBuilder;
    private final DataFormatterFacade dataFormatter;

    public TableSectionBuilder(
            DocumentBuilder documentBuilder,
            TableBuilderFacade tableBuilder,
            DataFormatterFacade dataFormatter
    ) {
        this.documentBuilder = documentBuilder;
        this.tableBuilder = tableBuilder;
        this.dataFormatter = dataFormatter;
    }

    /**
     * Adds transaction table section to document.
     *
     * @param document Target document
     * @param transactions List of transactions to display
     */
    public void addTransactionsTable(Document document, List<Transaction> transactions) {
        Table table = tableBuilder.buildTransactionsTable(transactions);
        document.add(table);
        documentBuilder.addSpacing(document);
    }

    /**
     * Adds summary section with currency totals to document.
     *
     * @param document Target document
     * @param transactions List of transactions to summarize
     */
    public void addSummarySection(Document document, List<Transaction> transactions) {
        documentBuilder.addSectionTitle(document, "Resumen Financiero");
        documentBuilder.addSectionDescription(document,
                "A continuación se presentan los totales acumulados de todas las transacciones agrupadas por moneda.");

        Map<String, BigDecimal> totalsByCurrency = dataFormatter.calculateTotalsByCurrency(transactions);
        Table summaryTable = tableBuilder.buildCurrencySummaryTable(totalsByCurrency);
        document.add(summaryTable);

        documentBuilder.addSpacing(document);
    }
}

