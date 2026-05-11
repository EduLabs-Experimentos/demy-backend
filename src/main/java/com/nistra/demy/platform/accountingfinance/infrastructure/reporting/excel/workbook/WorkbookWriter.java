package com.nistra.demy.platform.accountingfinance.infrastructure.reporting.excel.workbook;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Writes Excel workbooks to byte arrays.
 * <p>
 * Handles serialization of workbooks to byte format
 * for file download or storage.
 *
 * @author Salim Ramirez
 */
@Component
public class WorkbookWriter {

    /**
     * Writes workbook to byte array.
     *
     * @param workbook Workbook to write
     * @return Workbook as byte array
     * @throws RuntimeException if write fails
     */
    public byte[] writeToBytes(Workbook workbook) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Error writing workbook to bytes", e);
        }
    }
}

