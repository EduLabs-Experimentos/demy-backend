package com.nistra.demy.platform.accountingfinance.infrastructure.reporting.pdf.document;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;

/**
 * Factory for creating PDF document contexts.
 * <p>
 * Handles the low-level creation of PDF writers,
 * documents and their lifecycle management.
 *
 * @author Salim Ramirez
 */
@Component
public class DocumentFactory {

    /**
     * Creates a new PDF document context.
     *
     * @param outputStream Stream to write PDF content
     * @return DocumentContext wrapping PDF components
     */
    public DocumentContext createDocument(ByteArrayOutputStream outputStream) {
        try {
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);
            return new DocumentContext(writer, pdf, document);
        } catch (Exception e) {
            throw new RuntimeException("Error creating PDF document", e);
        }
    }

    /**
     * Context holder for PDF document components.
     * <p>
     * Implements AutoCloseable to ensure proper resource cleanup.
     */
    public static class DocumentContext implements AutoCloseable {
        private final PdfWriter writer;
        private final PdfDocument pdf;
        private final Document document;

        public DocumentContext(PdfWriter writer, PdfDocument pdf, Document document) {
            this.writer = writer;
            this.pdf = pdf;
            this.document = document;
        }

        public Document getDocument() {
            return document;
        }

        @Override
        public void close() {
            try {
                if (document != null) {
                    document.close();
                }
                if (pdf != null) {
                    pdf.close();
                }
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception e) {
                throw new RuntimeException("Error closing PDF document", e);
            }
        }
    }
}

