package com.nistra.demy.platform.accountingfinance.infrastructure.reporting.excel.adapter;

import com.nistra.demy.platform.accountingfinance.domain.model.aggregates.Transaction;
import com.nistra.demy.platform.accountingfinance.infrastructure.reporting.excel.PoiExcelReportService;
import com.nistra.demy.platform.accountingfinance.infrastructure.reporting.excel.table.TableBuilder;
import com.nistra.demy.platform.accountingfinance.infrastructure.reporting.excel.table.row.TransactionRowMapper;
import com.nistra.demy.platform.accountingfinance.infrastructure.reporting.excel.workbook.WorkbookBuilder;
import com.nistra.demy.platform.accountingfinance.infrastructure.reporting.excel.workbook.WorkbookFactory;
import com.nistra.demy.platform.accountingfinance.infrastructure.reporting.excel.formatter.DataFormatter;
import com.nistra.demy.platform.accountingfinance.infrastructure.reporting.excel.workbook.style.StyleManager;
import com.nistra.demy.platform.shared.domain.model.valueobjects.Money;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import java.util.Currency;
import java.util.List;
import java.util.Map;

/**
 * Adapter for Excel report generation using Apache POI.
 * <p>
 * Orchestrates workbook builder, table builder, style manager,
 * and row mapper to create comprehensive transaction Excel reports.
 *
 * @author Salim Ramirez
 */
@Service
public class PoiExcelReportServiceAdapter implements PoiExcelReportService {

    private final WorkbookBuilder workbookBuilder;
    private final TableBuilder tableBuilder;
    private final DataFormatter dataFormatter;
    private final StyleManager styleManager;
    private final TransactionRowMapper rowMapper;

    public PoiExcelReportServiceAdapter(
            WorkbookBuilder workbookBuilder,
            TableBuilder tableBuilder,
            DataFormatter dataFormatter,
            StyleManager styleManager,
            TransactionRowMapper rowMapper
    ) {
        this.workbookBuilder = workbookBuilder;
        this.tableBuilder = tableBuilder;
        this.dataFormatter = dataFormatter;
        this.styleManager = styleManager;
        this.rowMapper = rowMapper;
    }

    @Override
    public byte[] generateTransactionsExcelReport(List<Transaction> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            return generateEmptyReport();
        }

        try (WorkbookFactory.WorkbookContext context = workbookBuilder.createWorkbook("Transacciones")) {
            Workbook workbook = context.getWorkbook();
            Sheet sheet = context.getSheet();

            var uniqueCurrencies = dataFormatter.getUniqueCurrencies(transactions);

            CellStyle headerStyle = styleManager.createHeaderStyle(workbook);
            Map<Currency, CellStyle> currencyStyles = styleManager.createCurrencyStyles(workbook, uniqueCurrencies);
            Map<Currency, CellStyle> boldCurrencyStyles = styleManager.createBoldCurrencyStyles(workbook, uniqueCurrencies);

            tableBuilder.addHeaders(sheet, headerStyle);

            int lastRowIdx = addTransactionRows(sheet, transactions, currencyStyles);

            Map<Currency, Money> totalsByCurrency = dataFormatter.calculateTotalsByCurrency(transactions);

            tableBuilder.addTotalsByCurrencyTable(
                    sheet,
                    lastRowIdx,
                    totalsByCurrency,
                    headerStyle,
                    boldCurrencyStyles
            );

            workbookBuilder.autoSizeColumns(sheet, tableBuilder.getHeaderCount());

            return workbookBuilder.writeToBytes(workbook);

        } catch (Exception e) {
            throw new RuntimeException("Error generating Excel transactions report: " + e.getMessage(), e);
        }
    }

    private int addTransactionRows(
            Sheet sheet,
            List<Transaction> transactions,
            Map<Currency, CellStyle> currencyStyles
    ) {
        int rowIdx = 1;
        for (Transaction transaction : transactions) {
            Row row = sheet.createRow(rowIdx++);
            Currency currency = transaction.getAmount().currency();
            CellStyle currencyStyle = currencyStyles.get(currency);
            rowMapper.mapToRow(transaction, row, currencyStyle);
        }
        return rowIdx;
    }

    private byte[] generateEmptyReport() {
        try (WorkbookFactory.WorkbookContext context = workbookBuilder.createWorkbook("Sin Datos")) {
            Sheet sheet = context.getSheet();
            Row row = sheet.createRow(0);
            row.createCell(0).setCellValue("No se encontraron transacciones para los filtros seleccionados.");
            return workbookBuilder.writeToBytes(context.getWorkbook());
        } catch (Exception e) {
            throw new RuntimeException("Error generating empty report", e);
        }
    }
}
