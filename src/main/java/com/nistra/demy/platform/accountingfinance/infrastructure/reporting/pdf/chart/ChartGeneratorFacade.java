package com.nistra.demy.platform.accountingfinance.infrastructure.reporting.pdf.chart;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Facade for chart generation operations.
 * <p>
 * Coordinates specialized chart builders to provide a unified
 * interface for creating charts in PDF reports.
 *
 * @author Salim Ramirez
 */
@Component
public class ChartGeneratorFacade {

    private final PieChartBuilder pieChartBuilder;
    private final TimeSeriesChartBuilder timeSeriesChartBuilder;

    public ChartGeneratorFacade(PieChartBuilder pieChartBuilder, TimeSeriesChartBuilder timeSeriesChartBuilder) {
        this.pieChartBuilder = pieChartBuilder;
        this.timeSeriesChartBuilder = timeSeriesChartBuilder;
    }

    /**
     * Generates a pie chart as PNG image.
     *
     * @param data Map of category labels to numeric values
     * @param title Chart title
     * @return PNG image as byte array
     */
    public byte[] generatePieChart(Map<String, BigDecimal> data, String title) {
        return pieChartBuilder.generatePieChart(data, title);
    }

    /**
     * Generates a time series chart as PNG image.
     *
     * @param data Map of date strings to numeric values
     * @param title Chart title
     * @param xAxisTitle Label for X-axis
     * @param yAxisTitle Label for Y-axis
     * @return PNG image as byte array
     */
    public byte[] generateTimeSeriesChart(Map<String, BigDecimal> data, String title, String xAxisTitle, String yAxisTitle) {
        return timeSeriesChartBuilder.generateTimeSeriesChart(data, title, xAxisTitle, yAxisTitle);
    }
}

