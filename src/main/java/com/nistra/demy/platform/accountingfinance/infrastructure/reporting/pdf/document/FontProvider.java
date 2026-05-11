package com.nistra.demy.platform.accountingfinance.infrastructure.reporting.pdf.document;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import org.springframework.stereotype.Component;

/**
 * Provides PDF fonts for document creation.
 * <p>
 * Centralized font creation to ensure consistency
 * across all PDF document components.
 *
 * @author Salim Ramirez
 */
@Component
public class FontProvider {

    /**
     * Creates bold font.
     *
     * @return Bold Helvetica font
     */
    public PdfFont createBoldFont() {
        try {
            return PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        } catch (Exception e) {
            throw new RuntimeException("Error creating bold font", e);
        }
    }

    /**
     * Creates regular font.
     *
     * @return Regular Helvetica font
     */
    public PdfFont createRegularFont() {
        try {
            return PdfFontFactory.createFont(StandardFonts.HELVETICA);
        } catch (Exception e) {
            throw new RuntimeException("Error creating regular font", e);
        }
    }

    /**
     * Creates italic font.
     *
     * @return Italic Helvetica font
     */
    public PdfFont createItalicFont() {
        try {
            return PdfFontFactory.createFont(StandardFonts.HELVETICA_OBLIQUE);
        } catch (Exception e) {
            throw new RuntimeException("Error creating italic font", e);
        }
    }
}

