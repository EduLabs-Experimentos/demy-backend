package com.nistra.demy.platform.billing.domain.model.commands;

import com.nistra.demy.platform.billing.domain.model.valueobjects.InvoiceStatus;
import com.nistra.demy.platform.billing.domain.model.valueobjects.InvoiceType;
import com.nistra.demy.platform.shared.domain.model.valueobjects.Money;

import java.time.LocalDate;

public record AssignInvoiceToBillingAccountCommand(
         InvoiceType invoiceType,
         Money amount,
         String description,
         LocalDate issueDate,
         LocalDate dueDate,
         InvoiceStatus status,
         Long billingAccountId
) {
    public AssignInvoiceToBillingAccountCommand {
        if (invoiceType == null)
            throw new IllegalArgumentException("Invoice type cannot be null");
        if (amount == null)
            throw new IllegalArgumentException("Amount cannot be null");
        if (description == null || description.isBlank())
            throw new IllegalArgumentException("Description cannot be null or blank");
        if (issueDate == null)
            throw new IllegalArgumentException("Issue date cannot be null");
        if (dueDate == null)
            throw new IllegalArgumentException("Due date cannot be null");
        if (status == null)
            throw new IllegalArgumentException("Invoice status cannot be null");
        if (billingAccountId == null || billingAccountId <= 0)
            throw new IllegalArgumentException("Billing account ID cannot be null or less than or equal to zero");
    }
}
