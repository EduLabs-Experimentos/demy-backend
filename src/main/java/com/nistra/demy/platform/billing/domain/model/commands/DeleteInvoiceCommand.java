package com.nistra.demy.platform.billing.domain.model.commands;

public record DeleteInvoiceCommand(Long billingAccountId, Long invoiceId) { }