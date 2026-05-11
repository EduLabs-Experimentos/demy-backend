package com.nistra.demy.platform.accountingfinance.infrastructure.reporting.excel.table.header;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;

/**
 * Builds header rows for Excel transaction tables.
 * <p>
 * Manages the creation and styling of column headers
 * for transaction detail tables.
 *
 * @author Salim Ramirez
 */
@Component
public class HeaderBuilder {

    private static final String[] HEADERS = {
            "Fecha", "Tipo", "Categoría", "Método", "Monto", "Descripción"
    };

    /**
     * Adds header row to sheet.
     *
     * @param sheet Sheet to add headers to
     * @param headerStyle Style for header cells
     */
    public void addHeaders(Sheet sheet, CellStyle headerStyle) {
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < HEADERS.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(HEADERS[i]);
            cell.setCellStyle(headerStyle);
        }
    }

    /**
     * Gets the number of header columns.
     *
     * @return Header count
     */
    public int getHeaderCount() {
        return HEADERS.length;
    }
}

