package com.nistra.demy.platform.accountingfinance.infrastructure.reporting.pdf.adapter;

import com.nistra.demy.platform.accountingfinance.domain.model.aggregates.Transaction;
import com.nistra.demy.platform.accountingfinance.infrastructure.reporting.pdf.ITextPdfReportService;
import com.nistra.demy.platform.accountingfinance.infrastructure.reporting.pdf.document.DocumentBuilder;
import com.nistra.demy.platform.accountingfinance.infrastructure.reporting.pdf.document.DocumentFactory;
import com.nistra.demy.platform.accountingfinance.infrastructure.reporting.pdf.section.ChartSectionBuilder;
import com.nistra.demy.platform.accountingfinance.infrastructure.reporting.pdf.section.HeaderSectionBuilder;
import com.nistra.demy.platform.accountingfinance.infrastructure.reporting.pdf.section.TableSectionBuilder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Adapter for PDF report generation using iText library.
 * <p>
 * Orchestrates section builders to create comprehensive
 * transaction PDF reports with headers, tables, and charts.
 *
 * @author Salim Ramirez
 */
@Service
public class ITextPdfReportServiceAdapter implements ITextPdfReportService {

    private final DocumentBuilder documentBuilder;
    private final HeaderSectionBuilder headerBuilder;
    private final TableSectionBuilder tableBuilder;
    private final ChartSectionBuilder chartBuilder;

    public ITextPdfReportServiceAdapter(
            DocumentBuilder documentBuilder,
            HeaderSectionBuilder headerBuilder,
            TableSectionBuilder tableBuilder,
            ChartSectionBuilder chartBuilder
    ) {
        this.documentBuilder = documentBuilder;
        this.headerBuilder = headerBuilder;
        this.tableBuilder = tableBuilder;
        this.chartBuilder = chartBuilder;
    }

    @Override
    public byte[] generateTransactionsPdfReport(List<Transaction> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            return generateEmptyReport();
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try (DocumentFactory.DocumentContext context = documentBuilder.createDocument(outputStream)) {
            var document = context.getDocument();

            headerBuilder.addReportHeader(document);
            tableBuilder.addTransactionsTable(document, transactions);
            tableBuilder.addSummarySection(document, transactions);
            chartBuilder.addChartsSection(document, transactions);

        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF transactions report: " + e.getMessage(), e);
        }

        return outputStream.toByteArray();
    }

    private byte[] generateEmptyReport() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (DocumentFactory.DocumentContext context = documentBuilder.createDocument(outputStream)) {
            var document = context.getDocument();
            document.add(new com.itextpdf.layout.element.Paragraph(
                    "No se encontraron transacciones para los filtros seleccionados."
            ));
        } catch (Exception e) {
            throw new RuntimeException("Error generating empty report", e);
        }
        return outputStream.toByteArray();
    }
}

