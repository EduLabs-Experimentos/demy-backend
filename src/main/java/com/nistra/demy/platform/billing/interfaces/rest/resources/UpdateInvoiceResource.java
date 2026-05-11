package com.nistra.demy.platform.billing.interfaces.rest.resources;

public record UpdateInvoiceResource(
        String invoiceType,
        String amount,
        String currency,
        String description,
        String status
) { }