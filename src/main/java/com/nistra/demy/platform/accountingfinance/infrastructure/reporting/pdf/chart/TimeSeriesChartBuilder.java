package com.nistra.demy.platform.accountingfinance.infrastructure.reporting.pdf.chart;

import org.knowm.xchart.CategoryChart;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Builds time series charts for temporal data visualization.
 * <p>
 * Creates category charts showing data evolution over time
 * for financial analysis.
 *
 * @author Salim Ramirez
 */
@Component
public class TimeSeriesChartBuilder {

    private static final int CHART_WIDTH = 500;
    private static final int CHART_HEIGHT = 250;

    private final ChartStyler styleConfigurator;
    private final ImageConverter imageConverter;

    public TimeSeriesChartBuilder(ChartStyler styleConfigurator, ImageConverter imageConverter) {
        this.styleConfigurator = styleConfigurator;
        this.imageConverter = imageConverter;
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
        List<String> sortedDates = data.keySet().stream().sorted().collect(Collectors.toList());
        List<BigDecimal> values = sortedDates.stream().map(data::get).collect(Collectors.toList());

        CategoryChart timeChart = new org.knowm.xchart.CategoryChartBuilder()
                .width(CHART_WIDTH)
                .height(CHART_HEIGHT)
                .title(title)
                .xAxisTitle(xAxisTitle)
                .yAxisTitle(yAxisTitle)
                .build();

        styleConfigurator.configureCategoryChartStyle(timeChart.getStyler());
        timeChart.addSeries("Transacciones", sortedDates, values);

        return imageConverter.convertCategoryChartToBytes(timeChart);
    }
}

