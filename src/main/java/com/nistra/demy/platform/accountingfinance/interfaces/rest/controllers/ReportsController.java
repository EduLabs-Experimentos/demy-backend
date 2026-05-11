package com.nistra.demy.platform.accountingfinance.interfaces.rest.controllers;

import com.nistra.demy.platform.accountingfinance.domain.model.valueobjects.TransactionCategory;
import com.nistra.demy.platform.accountingfinance.domain.model.valueobjects.TransactionMethod;
import com.nistra.demy.platform.accountingfinance.domain.model.valueobjects.TransactionType;
import com.nistra.demy.platform.accountingfinance.domain.services.ReportCommandService;
import com.nistra.demy.platform.accountingfinance.interfaces.rest.transform.TransactionCategoryFromResourceAssembler;
import com.nistra.demy.platform.accountingfinance.interfaces.rest.transform.TransactionMethodResourceAssembler;
import com.nistra.demy.platform.accountingfinance.interfaces.rest.transform.TransactionTypeFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/reports/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Reports", description = "Endpoints for managing accounting reports")
public class ReportsController {

    private static final String EXCEL_MEDIA_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    private final ReportCommandService reportCommandService;

    public ReportsController(ReportCommandService reportCommandService) {
        this.reportCommandService = reportCommandService;
    }

    @Operation(
            summary = "Generate PDF Transaction Report",
            description = "Generates a PDF report of all transactions filtered by category, method, and type."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "PDF report generated successfully"),
            @ApiResponse(responseCode = "404", description = "No transactions found for the given filters")
    })
    @GetMapping(value = "/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generateTransactionsPdfReport(
            @Parameter(
                    description = "Filter by transaction category (e.g., STUDENT_ENROLLMENT, TEACHER_SALARY)",
                    schema = @Schema(implementation = TransactionCategory.class)
            )
            @RequestParam(required = false) String category,

            @Parameter(
                    description = "Filter by transaction method (e.g., CASH, CREDIT_CARD)",
                    schema = @Schema(implementation = TransactionMethod.class)
            )
            @RequestParam(required = false) String method,

            @Parameter(
                    description = "Filter by transaction type (e.g., INCOME, EXPENSE)",
                    schema = @Schema(implementation = TransactionType.class)
            )
            @RequestParam(required = false) String type
    ) {
        var categoryValueObject = TransactionCategoryFromResourceAssembler.toValueObjectFromString(category);
        var methodValueObject = TransactionMethodResourceAssembler.toValueObjectFromString(method);
        var typeValueObject = TransactionTypeFromResourceAssembler.toValueObjectFromString(type);
        var pdfBytes = reportCommandService.generateTransactionsPdfReport(
                categoryValueObject, methodValueObject, typeValueObject
        );
        if (pdfBytes == null || pdfBytes.length == 0) return ResponseEntity.notFound().build();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=transactions-report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    @GetMapping(value = "/excel", produces = EXCEL_MEDIA_TYPE)
    @Operation(
            summary = "Generate Excel Transaction Report",
            description = "Generates an Excel report of all transactions filtered by category, method, and type."
    )
    public ResponseEntity<byte[]> generateTransactionsExcelReport(
            @Parameter(
                    description = "Filter by transaction category (e.g., STUDENT_ENROLLMENT, TEACHER_SALARY)",
                    schema = @Schema(implementation = TransactionCategory.class)
            )
            @RequestParam(required = false) String category,

            @Parameter(
                    description = "Filter by transaction method (e.g., CASH, CREDIT_CARD)",
                    schema = @Schema(implementation = TransactionMethod.class)
            )
            @RequestParam(required = false) String method,

            @Parameter(
                    description = "Filter by transaction type (e.g., INCOME, EXPENSE)",
                    schema = @Schema(implementation = TransactionType.class)
            )
            @RequestParam(required = false) String type
    ) {
        var categoryValueObject = TransactionCategoryFromResourceAssembler.toValueObjectFromString(category);
        var methodValueObject = TransactionMethodResourceAssembler.toValueObjectFromString(method);
        var typeValueObject = TransactionTypeFromResourceAssembler.toValueObjectFromString(type);
        var excelBytes = reportCommandService.generateTransactionsExcelReport(categoryValueObject, methodValueObject, typeValueObject);
        if (excelBytes == null || excelBytes.length == 0)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=transactions-report.xlsx")
                .contentType(MediaType.parseMediaType(EXCEL_MEDIA_TYPE))
                .body(excelBytes);
    }
}