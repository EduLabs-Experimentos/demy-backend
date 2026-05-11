package com.nistra.demy.platform.accountingfinance.infrastructure.reporting.pdf.section;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.nistra.demy.platform.accountingfinance.domain.model.aggregates.Transaction;
import com.nistra.demy.platform.accountingfinance.infrastructure.reporting.pdf.chart.ChartGeneratorFacade;
import com.nistra.demy.platform.accountingfinance.infrastructure.reporting.pdf.document.DocumentBuilder;
import com.nistra.demy.platform.accountingfinance.infrastructure.reporting.pdf.formatter.DataFormatterFacade;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Builds chart sections for PDF reports.
 * <p>
 * Handles the creation of chart visualizations including
 * expense distribution and income evolution charts.
 *
 * @author Salim Ramirez
 */
@Component
public class ChartSectionBuilder {

    private final DocumentBuilder documentBuilder;
    private final ChartGeneratorFacade chartGenerator;
    private final DataFormatterFacade dataFormatter;

    public ChartSectionBuilder(
            DocumentBuilder documentBuilder,
            ChartGeneratorFacade chartGenerator,
            DataFormatterFacade dataFormatter
    ) {
        this.documentBuilder = documentBuilder;
        this.chartGenerator = chartGenerator;
        this.dataFormatter = dataFormatter;
    }

    /**
     * Adds charts section to document with expense and income visualizations.
     *
     * @param document Target document
     * @param transactions List of transactions to analyze
     */
    public void addChartsSection(Document document, List<Transaction> transactions) {
        document.add(new AreaBreak());

        documentBuilder.addSectionTitle(document, "Análisis Gráfico");
        documentBuilder.addSpacing(document);

        addExpensesPieChart(document, transactions);
        addIncomeTimeSeriesChart(document, transactions);
    }

    private void addExpensesPieChart(Document document, List<Transaction> transactions) {
        Map<String, BigDecimal> expensesByCategory = dataFormatter.calculateExpenseTotalsByCategory(transactions);

        if (!expensesByCategory.isEmpty()) {
            documentBuilder.addSubtitle(document, "Distribución de Gastos por Categoría");
            documentBuilder.addSectionDescription(document,
                    "El siguiente gráfico circular muestra la distribución porcentual de todos los gastos " +
                    "registrados, agrupados por categoría. Esto permite identificar las áreas de mayor inversión.");

            byte[] pieChartBytes = chartGenerator.generatePieChart(
                    expensesByCategory,
                    "Gastos por Categoría"
            );
            documentBuilder.addImage(document, pieChartBytes, 5, 10);
        }
    }

    private void addIncomeTimeSeriesChart(Document document, List<Transaction> transactions) {
        Map<String, BigDecimal> incomesOverTime = dataFormatter.calculateIncomeTotalsOverTime(transactions);

        if (!incomesOverTime.isEmpty()) {
            documentBuilder.addSubtitle(document, "Evolución Temporal de Ingresos");
            documentBuilder.addSectionDescription(document,
                    "El siguiente gráfico de barras presenta la evolución de los ingresos a lo largo del tiempo, " +
                    "permitiendo identificar tendencias y patrones en la generación de ingresos.");

            byte[] timeChartBytes = chartGenerator.generateTimeSeriesChart(
                    incomesOverTime,
                    "Evolución de Ingresos en el Tiempo",
                    "Fecha",
                    "Monto Total"
            );
            documentBuilder.addImage(document, timeChartBytes, 5, 5);
        }
    }
}

