package com.nistra.demy.platform.accountingfinance.infrastructure.reporting.pdf.chart;

import org.knowm.xchart.PieChart;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Builds pie charts for financial data visualization.
 * <p>
 * Creates styled pie charts showing distribution
 * of values across categories.
 *
 * @author Salim Ramirez
 */
@Component
public class PieChartBuilder {

    private static final int CHART_WIDTH = 400;
    private static final int CHART_HEIGHT = 250;

    private final ChartStyler styleConfigurator;
    private final ImageConverter imageConverter;

    public PieChartBuilder(ChartStyler styleConfigurator, ImageConverter imageConverter) {
        this.styleConfigurator = styleConfigurator;
        this.imageConverter = imageConverter;
    }

    /**
     * Generates a pie chart as PNG image.
     *
     * @param data Map of category labels to numeric values
     * @param title Chart title
     * @return PNG image as byte array
     */
    public byte[] generatePieChart(Map<String, BigDecimal> data, String title) {
        PieChart pieChart = new org.knowm.xchart.PieChartBuilder()
                .width(CHART_WIDTH)
                .height(CHART_HEIGHT)
                .title(title)
                .build();

        styleConfigurator.configurePieChartStyle(pieChart.getStyler());
        data.forEach(pieChart::addSeries);

        return imageConverter.convertPieChartToBytes(pieChart);
    }
}

