package com.nistra.demy.platform.accountingfinance.infrastructure.reporting.pdf.document;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.properties.HorizontalAlignment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

/**
 * Writes images to PDF documents.
 * <p>
 * Handles adding logos and chart images to documents
 * with appropriate positioning and margins.
 *
 * @author Salim Ramirez
 */
@Component
public class ImageWriter {

    private static final String LOGO_PATH = "static/images/demy-combination-mark-original.png";

    /**
     * Adds company logo to document.
     *
     * @param document Target document
     */
    public void addLogo(Document document) {
        try {
            ClassPathResource resource = new ClassPathResource(LOGO_PATH);
            byte[] imageBytes = resource.getContentAsByteArray();
            Image logo = new Image(ImageDataFactory.create(imageBytes))
                    .scaleToFit(80, 80)
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(logo);
        } catch (Exception e) {
            throw new RuntimeException("Error adding logo to PDF", e);
        }
    }

    /**
     * Adds image to document.
     *
     * @param document Target document
     * @param imageBytes Image data
     * @param marginTop Top margin
     * @param marginBottom Bottom margin
     */
    public void addImage(Document document, byte[] imageBytes, float marginTop, float marginBottom) {
        try {
            document.add(new Image(ImageDataFactory.create(imageBytes))
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setMarginTop(marginTop)
                    .setMarginBottom(marginBottom));
        } catch (Exception e) {
            throw new RuntimeException("Error adding image to PDF", e);
        }
    }
}

