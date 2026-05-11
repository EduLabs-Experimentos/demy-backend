package com.nistra.demy.platform.accountingfinance.infrastructure.reporting.pdf;

import com.nistra.demy.platform.accountingfinance.application.internal.outboundservices.reporting.PdfReportingService;

/**
 * Infrastructure interface for iText-based PDF report generation.
 * <p>
 * Extends the application-layer PdfReportingService to provide
 * infrastructure-specific contract for report generation.
 *
 * @author Salim Ramirez
 */
public interface ITextPdfReportService extends PdfReportingService {
}
