package com.nistra.demy.platform.billing.domain.model.commands;

public record MarkInvoiceAsPaidCommand(
        Long billingAccountId,
        Long invoiceId
) {
}
