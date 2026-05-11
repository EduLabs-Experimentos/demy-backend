package com.nistra.demy.platform.accountingfinance.infrastructure.reporting.pdf.section;

import com.itextpdf.layout.Document;
import com.nistra.demy.platform.accountingfinance.infrastructure.reporting.pdf.document.DocumentBuilder;
import org.springframework.stereotype.Component;

/**
 * Builds header sections for PDF reports.
 * <p>
 * Handles the creation of report headers including logo,
 * title, description, and generation date.
 *
 * @author Salim Ramirez
 */
@Component
public class HeaderSectionBuilder {

    private final DocumentBuilder documentBuilder;

    public HeaderSectionBuilder(DocumentBuilder documentBuilder) {
        this.documentBuilder = documentBuilder;
    }

    /**
     * Adds standard report header to document.
     *
     * @param document Target document
     */
    public void addReportHeader(Document document) {
        documentBuilder.addLogo(document);
        documentBuilder.addSpacing(document);
        documentBuilder.addTitle(document, "Reporte de Transacciones");
        documentBuilder.addDescription(document,
                "Este documento presenta un análisis detallado de las transacciones registradas, " +
                "incluyendo información general, totales por moneda y análisis gráfico de gastos e ingresos.");
        documentBuilder.addGeneratedDate(document);
        documentBuilder.addSpacing(document);
    }
}

