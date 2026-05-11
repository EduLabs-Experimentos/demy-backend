package com.nistra.demy.platform.accountingfinance.infrastructure.reporting.excel.workbook;

import com.nistra.demy.platform.accountingfinance.infrastructure.reporting.excel.workbook.style.CellStyleFactory;
import com.nistra.demy.platform.accountingfinance.infrastructure.reporting.excel.column.ColumnSizeAdjuster;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;

import java.util.Currency;

/**
 * Facade for Excel workbook building operations.
 * <p>
 * Coordinates factory, style factory, column adjuster, and writer
 * to provide a unified interface for workbook creation.
 *
 * @author Salim Ramirez
 */
@Component
public class WorkbookBuilder {

    private final WorkbookFactory workbookFactory;
    private final CellStyleFactory styleFactory;
    private final ColumnSizeAdjuster columnAdjuster;
    private final WorkbookWriter workbookWriter;

    public WorkbookBuilder(
            WorkbookFactory workbookFactory,
            CellStyleFactory styleFactory,
            ColumnSizeAdjuster columnAdjuster,
            WorkbookWriter workbookWriter
    ) {
        this.workbookFactory = workbookFactory;
        this.styleFactory = styleFactory;
        this.columnAdjuster = columnAdjuster;
        this.workbookWriter = workbookWriter;
    }

    /**
     * Creates a new Excel workbook with sheet.
     *
     * @param sheetName Name for the sheet
     * @return WorkbookContext with workbook and sheet
     */
    public WorkbookFactory.WorkbookContext createWorkbook(String sheetName) {
        styleFactory.clearCache();
        return workbookFactory.createWorkbook(sheetName);
    }

    /**
     * Creates header cell style.
     *
     * @param workbook Workbook to create style in
     * @return Header style
     */
    public CellStyle createHeaderStyle(Workbook workbook) {
        return styleFactory.createHeaderStyle(workbook);
    }

    /**
     * Creates text cell style.
     *
     * @param workbook Workbook to create style in
     * @return Text style
     */
    public CellStyle createTextStyle(Workbook workbook) {
        return styleFactory.createTextStyle(workbook);
    }

    /**
     * Creates number cell style.
     *
     * @param workbook Workbook to create style in
     * @return Number style
     */
    public CellStyle createNumberStyle(Workbook workbook) {
        return styleFactory.createNumberStyle(workbook);
    }

    /**
     * Creates currency cell style.
     *
     * @param workbook Workbook to create style in
     * @param currency Currency for formatting
     * @return Currency style
     */
    public CellStyle createCurrencyStyle(Workbook workbook, Currency currency) {
        return styleFactory.createCurrencyStyle(workbook, currency);
    }

    /**
     * Creates bold currency cell style.
     *
     * @param workbook Workbook to create style in
     * @param currency Currency for formatting
     * @return Bold currency style
     */
    public CellStyle createBoldCurrencyStyle(Workbook workbook, Currency currency) {
        return styleFactory.createBoldCurrencyStyle(workbook, currency);
    }

    /**
     * Auto-sizes columns in sheet.
     *
     * @param sheet Sheet to adjust
     * @param columnCount Number of columns
     */
    public void autoSizeColumns(Sheet sheet, int columnCount) {
        columnAdjuster.autoSizeColumns(sheet, columnCount);
    }

    /**
     * Writes workbook to byte array.
     *
     * @param workbook Workbook to write
     * @return Workbook as bytes
     */
    public byte[] writeToBytes(Workbook workbook) {
        return workbookWriter.writeToBytes(workbook);
    }
}

