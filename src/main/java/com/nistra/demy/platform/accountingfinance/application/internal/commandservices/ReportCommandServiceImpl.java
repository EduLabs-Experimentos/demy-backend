package com.nistra.demy.platform.accountingfinance.application.internal.commandservices;

import com.nistra.demy.platform.accountingfinance.application.internal.outboundservices.reporting.ExcelReportingService;
import com.nistra.demy.platform.accountingfinance.application.internal.outboundservices.reporting.PdfReportingService;
import com.nistra.demy.platform.accountingfinance.domain.model.queries.GetAllTransactionsQuery;
import com.nistra.demy.platform.accountingfinance.domain.model.valueobjects.TransactionCategory;
import com.nistra.demy.platform.accountingfinance.domain.model.valueobjects.TransactionMethod;
import com.nistra.demy.platform.accountingfinance.domain.model.valueobjects.TransactionType;
import com.nistra.demy.platform.accountingfinance.domain.services.ReportCommandService;
import com.nistra.demy.platform.accountingfinance.domain.services.TransactionQueryService;
import org.springframework.stereotype.Service;

/**
 * Implementation of report command service.
 * <p>
 * Orchestrates transaction queries and delegates report generation
 * to PDF and Excel reporting services based on requested format.
 *
 * @author Salim Ramirez
 */
@Service
public class ReportCommandServiceImpl implements ReportCommandService {

    private final TransactionQueryService transactionQueryService;
    private final PdfReportingService pdfReportingService;
    private final ExcelReportingService excelReportingService;

    public ReportCommandServiceImpl(
            TransactionQueryService transactionQueryService,
            PdfReportingService pdfReportingService,
            ExcelReportingService excelReportingService
    ) {
        this.transactionQueryService = transactionQueryService;
        this.pdfReportingService = pdfReportingService;
        this.excelReportingService = excelReportingService;
    }

    /**
     * Generates a PDF report of transactions with optional filters.
     *
     * @param category Filter by transaction category (nullable)
     * @param method Filter by transaction method (nullable)
     * @param type Filter by transaction type (nullable)
     * @return PDF report as byte array
     */
    @Override
    public byte[] generateTransactionsPdfReport(
            TransactionCategory category,
            TransactionMethod method,
            TransactionType type
    ) {
        var query = new GetAllTransactionsQuery(category, method, type);
        var transactions = transactionQueryService.handle(query);
        return pdfReportingService.generateTransactionsPdfReport(transactions);
    }

    /**
     * Generates an Excel report of transactions with optional filters.
     *
     * @param category Filter by transaction category (nullable)
     * @param method Filter by transaction method (nullable)
     * @param type Filter by transaction type (nullable)
     * @return Excel report as byte array
     */
    @Override
    public byte[] generateTransactionsExcelReport(
            TransactionCategory category,
            TransactionMethod method,
            TransactionType type
    ) {
        var query = new GetAllTransactionsQuery(category, method, type);
        var transactions = transactionQueryService.handle(query);
        return excelReportingService.generateTransactionsExcelReport(transactions);
    }
}
