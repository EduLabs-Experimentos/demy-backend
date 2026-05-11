package com.nistra.demy.platform.accountingfinance.infrastructure.reporting.excel.workbook;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Factory for creating Excel workbooks and contexts.
 * <p>
 * Handles the creation of XLSX workbooks and manages
 * their lifecycle through WorkbookContext.
 *
 * @author Salim Ramirez
 */
@Component
public class WorkbookFactory {

    /**
     * Creates a new Excel workbook with sheet.
     *
     * @param sheetName Name for the sheet
     * @return WorkbookContext with workbook and sheet
     */
    public WorkbookContext createWorkbook(String sheetName) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(sheetName);
        return new WorkbookContext(workbook, sheet);
    }

    /**
     * Context holder for workbook and sheet.
     * <p>
     * Implements AutoCloseable for proper resource cleanup.
     */
    public static class WorkbookContext implements AutoCloseable {
        private final Workbook workbook;
        private final Sheet sheet;

        public WorkbookContext(Workbook workbook, Sheet sheet) {
            this.workbook = workbook;
            this.sheet = sheet;
        }

        public Workbook getWorkbook() {
            return workbook;
        }

        public Sheet getSheet() {
            return sheet;
        }

        @Override
        public void close() {
            try {
                if (workbook != null) {
                    workbook.close();
                }
            } catch (IOException e) {
                throw new RuntimeException("Error closing workbook", e);
            }
        }
    }
}

