package com.nistra.demy.platform.accountingfinance.infrastructure.reporting.pdf.document;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Writes text content to PDF documents.
 * <p>
 * Handles adding titles, descriptions, sections,
 * and formatted text elements to documents.
 *
 * @author Salim Ramirez
 */
@Component
public class ContentWriter {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final FontProvider fontProvider;

    public ContentWriter(FontProvider fontProvider) {
        this.fontProvider = fontProvider;
    }

    /**
     * Adds centered title to document.
     *
     * @param document Target document
     * @param titleText Title text
     */
    public void addTitle(Document document, String titleText) {
        Paragraph title = new Paragraph(titleText)
                .setFont(fontProvider.createBoldFont())
                .setFontSize(16)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(5);
        document.add(title);
    }

    /**
     * Adds centered description text.
     *
     * @param document Target document
     * @param descriptionText Description text
     */
    public void addDescription(Document document, String descriptionText) {
        Paragraph description = new Paragraph(descriptionText)
                .setFont(fontProvider.createItalicFont())
                .setFontSize(10)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(10);
        document.add(description);
    }

    /**
     * Adds left-aligned subtitle.
     *
     * @param document Target document
     * @param subtitleText Subtitle text
     */
    public void addSubtitle(Document document, String subtitleText) {
        Paragraph subtitle = new Paragraph(subtitleText)
                .setFont(fontProvider.createBoldFont())
                .setFontSize(14)
                .setTextAlignment(TextAlignment.LEFT)
                .setMarginBottom(5)
                .setMarginTop(10);
        document.add(subtitle);
    }

    /**
     * Adds section description text.
     *
     * @param document Target document
     * @param text Description text
     */
    public void addSectionDescription(Document document, String text) {
        Paragraph sectionDesc = new Paragraph(text)
                .setFont(fontProvider.createItalicFont())
                .setFontSize(10)
                .setTextAlignment(TextAlignment.LEFT)
                .setMarginBottom(10);
        document.add(sectionDesc);
    }

    /**
     * Adds section title.
     *
     * @param document Target document
     * @param text Title text
     */
    public void addSectionTitle(Document document, String text) {
        document.add(new Paragraph(text)
                .setFont(fontProvider.createBoldFont())
                .setFontSize(12)
                .setTextAlignment(TextAlignment.LEFT));
    }

    /**
     * Adds bullet point text.
     *
     * @param document Target document
     * @param text Bullet text
     */
    public void addBulletPoint(Document document, String text) {
        document.add(new Paragraph(" - " + text)
                .setFont(fontProvider.createRegularFont())
                .setFontSize(11)
                .setMarginLeft(20));
    }

    /**
     * Adds current date to document.
     *
     * @param document Target document
     */
    public void addGeneratedDate(Document document) {
        Paragraph generatedAt = new Paragraph("Generado el: " +
                LocalDate.now().format(DATE_FORMATTER))
                .setFont(fontProvider.createRegularFont())
                .setFontSize(10)
                .setTextAlignment(TextAlignment.RIGHT);
        document.add(generatedAt);
    }

    /**
     * Adds vertical spacing.
     *
     * @param document Target document
     */
    public void addSpacing(Document document) {
        document.add(new Paragraph("\n"));
    }
}

