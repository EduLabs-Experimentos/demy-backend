package com.nistra.demy.platform.accountingfinance.infrastructure.reporting.pdf.document;

import com.itextpdf.layout.Document;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;

/**
 * Facade for PDF document building operations.
 * <p>
 * Coordinates factory, content writer, and image writer
 * to provide a unified interface for document creation.
 *
 * @author Salim Ramirez
 */
@Component
public class DocumentBuilder {

    private final DocumentFactory documentFactory;
    private final ContentWriter contentWriter;
    private final ImageWriter imageWriter;

    public DocumentBuilder(
            DocumentFactory documentFactory,
            ContentWriter contentWriter,
            ImageWriter imageWriter
    ) {
        this.documentFactory = documentFactory;
        this.contentWriter = contentWriter;
        this.imageWriter = imageWriter;
    }

    /**
     * Creates a new PDF document context.
     *
     * @param outputStream Stream to write PDF content
     * @return DocumentContext wrapping PDF components
     */
    public DocumentFactory.DocumentContext createDocument(ByteArrayOutputStream outputStream) {
        return documentFactory.createDocument(outputStream);
    }

    /**
     * Adds company logo to document.
     *
     * @param document Target document
     */
    public void addLogo(Document document) {
        imageWriter.addLogo(document);
    }

    /**
     * Adds centered title to document.
     *
     * @param document Target document
     * @param titleText Title text
     */
    public void addTitle(Document document, String titleText) {
        contentWriter.addTitle(document, titleText);
    }

    /**
     * Adds centered description text.
     *
     * @param document Target document
     * @param descriptionText Description text
     */
    public void addDescription(Document document, String descriptionText) {
        contentWriter.addDescription(document, descriptionText);
    }

    /**
     * Adds left-aligned subtitle.
     *
     * @param document Target document
     * @param subtitleText Subtitle text
     */
    public void addSubtitle(Document document, String subtitleText) {
        contentWriter.addSubtitle(document, subtitleText);
    }

    /**
     * Adds section description text.
     *
     * @param document Target document
     * @param text Description text
     */
    public void addSectionDescription(Document document, String text) {
        contentWriter.addSectionDescription(document, text);
    }

    /**
     * Adds current date to document.
     *
     * @param document Target document
     */
    public void addGeneratedDate(Document document) {
        contentWriter.addGeneratedDate(document);
    }

    /**
     * Adds vertical spacing.
     *
     * @param document Target document
     */
    public void addSpacing(Document document) {
        contentWriter.addSpacing(document);
    }

    /**
     * Adds section title.
     *
     * @param document Target document
     * @param text Title text
     */
    public void addSectionTitle(Document document, String text) {
        contentWriter.addSectionTitle(document, text);
    }

    /**
     * Adds bullet point text.
     *
     * @param document Target document
     * @param text Bullet text
     */
    public void addBulletPoint(Document document, String text) {
        contentWriter.addBulletPoint(document, text);
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
        imageWriter.addImage(document, imageBytes, marginTop, marginBottom);
    }
}

