package com.nistra.demy.platform.billing.interfaces.rest.resources;

public record InvoiceResource(
        Long id,
        String invoiceType,
        String amount,
        String currency,
        String description,
        String issueDate,
        String dueDate,
        String status
) {
}
