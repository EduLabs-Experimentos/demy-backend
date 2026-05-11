package com.nistra.demy.platform.accountingfinance.infrastructure.reporting.pdf.chart;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.PieChart;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;

/**
 * Converts chart objects to PNG image bytes.
 * <p>
 * Handles the serialization of XChart objects into
 * PNG format for embedding in PDF documents.
 *
 * @author Salim Ramirez
 */
@Component
public class ImageConverter {

    /**
     * Converts pie chart to PNG byte array.
     *
     * @param chart Pie chart to convert
     * @return PNG image bytes
     */
    public byte[] convertPieChartToBytes(PieChart chart) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BitmapEncoder.saveBitmap(chart, outputStream, BitmapEncoder.BitmapFormat.PNG);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error converting pie chart to bytes", e);
        }
    }

    /**
     * Converts category chart to PNG byte array.
     *
     * @param chart Category chart to convert
     * @return PNG image bytes
     */
    public byte[] convertCategoryChartToBytes(CategoryChart chart) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BitmapEncoder.saveBitmap(chart, outputStream, BitmapEncoder.BitmapFormat.PNG);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error converting category chart to bytes", e);
        }
    }
}

