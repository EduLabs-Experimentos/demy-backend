package com.nistra.demy.platform.accountingfinance.infrastructure.reporting.pdf.table;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import org.springframework.stereotype.Component;

/**
 * Styles PDF table cells with consistent formatting.
 * <p>
 * Provides methods to create styled header and data cells
 * for different table types.
 *
 * @author Salim Ramirez
 */
@Component
public class CellStyler {

    private static final java.awt.Color HEADER_BACKGROUND_COLOR = new java.awt.Color(187, 222, 251);

    /**
     * Creates a styled header cell for transaction tables.
     *
     * @param text Header text
     * @return Styled header cell
     */
    public Cell createTransactionHeaderCell(String text) {
        return new Cell()
                .add(new Paragraph(text)
                        .setFont(createBoldFont())
                        .setFontSize(9)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setFontColor(ColorConstants.BLACK))
                .setBackgroundColor(new DeviceRgb(
                        HEADER_BACKGROUND_COLOR.getRed(),
                        HEADER_BACKGROUND_COLOR.getGreen(),
                        HEADER_BACKGROUND_COLOR.getBlue()))
                .simulateBold()
                .setPadding(3);
    }

    /**
     * Creates a styled data cell for transaction tables.
     *
     * @param text Cell text
     * @param alignment Text alignment
     * @return Styled data cell
     */
    public Cell createTransactionDataCell(String text, TextAlignment alignment) {
        return new Cell()
                .add(new Paragraph(text)
                        .setFont(createRegularFont())
                        .setFontSize(8)
                        .setFontColor(ColorConstants.BLACK)
                        .setTextAlignment(alignment))
                .setPadding(3)
                .setBackgroundColor(ColorConstants.WHITE);
    }

    /**
     * Creates a styled header cell for summary tables.
     *
     * @param text Header text
     * @return Styled header cell
     */
    public Cell createSummaryHeaderCell(String text) {
        return new Cell()
                .add(new Paragraph(text)
                        .setFont(createBoldFont())
                        .setFontSize(10)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setFontColor(ColorConstants.BLACK))
                .setBackgroundColor(new DeviceRgb(
                        HEADER_BACKGROUND_COLOR.getRed(),
                        HEADER_BACKGROUND_COLOR.getGreen(),
                        HEADER_BACKGROUND_COLOR.getBlue()))
                .setPadding(5);
    }

    /**
     * Creates a styled data cell for summary tables.
     *
     * @param text Cell text
     * @param alignment Text alignment
     * @return Styled data cell
     */
    public Cell createSummaryDataCell(String text, TextAlignment alignment) {
        return new Cell()
                .add(new Paragraph(text)
                        .setFont(createRegularFont())
                        .setFontSize(9)
                        .setFontColor(ColorConstants.BLACK)
                        .setTextAlignment(alignment))
                .setPadding(5)
                .setBackgroundColor(ColorConstants.WHITE);
    }

    private PdfFont createBoldFont() {
        try {
            return PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        } catch (Exception e) {
            throw new RuntimeException("Error creating bold font", e);
        }
    }

    private PdfFont createRegularFont() {
        try {
            return PdfFontFactory.createFont(StandardFonts.HELVETICA);
        } catch (Exception e) {
            throw new RuntimeException("Error creating regular font", e);
        }
    }
}

