package com.nistra.demy.platform.accountingfinance.infrastructure.reporting.excel;

import com.nistra.demy.platform.accountingfinance.application.internal.outboundservices.reporting.ExcelReportingService;

/**
 * Infrastructure interface for POI-based Excel report generation.
 * <p>
 * Extends the application-layer ExcelReportingService to provide
 * infrastructure-specific contract for Excel report generation.
 *
 * @author Salim Ramirez
 */
public interface PoiExcelReportService extends ExcelReportingService {
}
