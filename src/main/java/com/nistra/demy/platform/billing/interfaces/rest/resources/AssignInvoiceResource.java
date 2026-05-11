package com.nistra.demy.platform.billing.interfaces.rest.resources;

import java.time.LocalDate;

public record AssignInvoiceResource(
        String invoiceType,
        String amount,
        String currency,
        String description,
        LocalDate issueDate,
        LocalDate dueDate,
        String status
) {
    public AssignInvoiceResource {
        if (invoiceType == null || invoiceType.isBlank())
            throw new IllegalArgumentException("Invoice type cannot be null or blank");
        if (amount == null || amount.isBlank())
            throw new IllegalArgumentException("Amount cannot be null or blank");
        if (currency == null || currency.isBlank())
            throw new IllegalArgumentException("Currency cannot be null or blank");
        if (description == null || description.isBlank())
            throw new IllegalArgumentException("Description cannot be null or blank");
        if (issueDate == null)
            throw new IllegalArgumentException("Issue date cannot be null");
        if (dueDate == null)
            throw new IllegalArgumentException("Due date cannot be null");
        if (status == null || status.isBlank())
            throw new IllegalArgumentException("Status cannot be null or blank");
    }
}
