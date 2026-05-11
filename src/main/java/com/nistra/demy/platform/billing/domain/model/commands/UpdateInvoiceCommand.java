package com.nistra.demy.platform.billing.domain.model.commands;

import com.nistra.demy.platform.billing.domain.model.valueobjects.InvoiceStatus;
import com.nistra.demy.platform.billing.domain.model.valueobjects.InvoiceType;
import com.nistra.demy.platform.shared.domain.model.valueobjects.Money;

public record UpdateInvoiceCommand(
        Long billingAccountId,
        Long invoiceId,
        InvoiceType invoiceType,
        Money amount,
        String description,
        InvoiceStatus status
) { }