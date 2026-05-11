package com.nistra.demy.platform.billing.interfaces.rest.resources;

import java.util.List;

public record BillingAccountResource(
        Long id,
        Long studentId,
        String dniNumber,
        Long academyId,
        List<InvoiceResource> invoices
) {
}
