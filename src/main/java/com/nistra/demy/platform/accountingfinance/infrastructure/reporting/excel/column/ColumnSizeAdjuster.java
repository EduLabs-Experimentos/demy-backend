package com.nistra.demy.platform.accountingfinance.infrastructure.reporting.excel.column;

import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;

/**
 * Adjusts column sizes for Excel sheets.
 * <p>
 * Handles auto-sizing of columns and specific adjustments
 * for better readability in Excel reports.
 *
 * @author Salim Ramirez
 */
@Component
public class ColumnSizeAdjuster {

    private static final double TYPE_COLUMN_MULTIPLIER = 1.3;
    private static final double CATEGORY_COLUMN_MULTIPLIER = 1.2;
    private static final int TYPE_COLUMN_INDEX = 1;
    private static final int CATEGORY_COLUMN_INDEX = 2;

    /**
     * Auto-sizes columns with specific adjustments.
     *
     * @param sheet Sheet to adjust
     * @param columnCount Number of columns to adjust
     */
    public void autoSizeColumns(Sheet sheet, int columnCount) {
        for (int i = 0; i < columnCount; i++) {
            sheet.autoSizeColumn(i);
        }

        adjustTypeColumn(sheet);
        adjustCategoryColumn(sheet);
    }

    private void adjustTypeColumn(Sheet sheet) {
        int columnWidth = sheet.getColumnWidth(TYPE_COLUMN_INDEX);
        sheet.setColumnWidth(TYPE_COLUMN_INDEX, (int) (columnWidth * TYPE_COLUMN_MULTIPLIER));
    }

    private void adjustCategoryColumn(Sheet sheet) {
        int columnWidth = sheet.getColumnWidth(CATEGORY_COLUMN_INDEX);
        sheet.setColumnWidth(CATEGORY_COLUMN_INDEX, (int) (columnWidth * CATEGORY_COLUMN_MULTIPLIER));
    }
}

