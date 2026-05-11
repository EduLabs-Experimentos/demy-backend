package com.nistra.demy.platform.accountingfinance.infrastructure.reporting.pdf.chart;

import org.knowm.xchart.style.CategoryStyler;
import org.knowm.xchart.style.PieStyler;
import org.springframework.stereotype.Component;

import java.awt.*;

/**
 * Configures visual styles for charts.
 * <p>
 * Provides consistent styling configuration for pie charts
 * and category charts used in PDF reports.
 *
 * @author Salim Ramirez
 */
@Component
public class ChartStyler {

    private static final Color[] PIE_CHART_COLORS = {
            new Color(93, 173, 226),
            new Color(241, 148, 138),
            new Color(130, 224, 170),
            new Color(247, 220, 111),
            new Color(165, 105, 189)
    };

    private static final Color LINE_CHART_COLOR = new Color(52, 152, 219);

    /**
     * Configures visual style for pie charts.
     *
     * @param style Pie chart styler to configure
     */
    public void configurePieChartStyle(PieStyler style) {
        style.setPlotBackgroundColor(Color.WHITE);
        style.setChartBackgroundColor(Color.WHITE);
        style.setLegendBackgroundColor(Color.WHITE);
        style.setChartFontColor(Color.DARK_GRAY);
        style.setSeriesColors(PIE_CHART_COLORS);
        style.setLegendVisible(true);
        style.setPlotBorderVisible(false);
        style.setChartTitleFont(new Font("Helvetica", Font.BOLD, 14));
        style.setLegendFont(new Font("Helvetica", Font.PLAIN, 10));
    }

    /**
     * Configures visual style for category/time series charts.
     *
     * @param style Category chart styler to configure
     */
    public void configureCategoryChartStyle(CategoryStyler style) {
        style.setPlotBackgroundColor(Color.WHITE);
        style.setChartBackgroundColor(Color.WHITE);
        style.setChartFontColor(Color.DARK_GRAY);
        style.setLegendVisible(false);
        style.setLabelsVisible(true);
        style.setPlotGridVerticalLinesVisible(false);
        style.setPlotGridHorizontalLinesVisible(true);
        style.setPlotBorderVisible(false);
        style.setAxisTickLabelsFont(new Font("Helvetica", Font.PLAIN, 9));
        style.setSeriesColors(new Color[]{LINE_CHART_COLOR});
    }
}

