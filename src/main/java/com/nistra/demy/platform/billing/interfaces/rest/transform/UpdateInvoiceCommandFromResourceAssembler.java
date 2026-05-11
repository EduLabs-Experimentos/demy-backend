package com.nistra.demy.platform.billing.interfaces.rest.transform;

import com.nistra.demy.platform.billing.domain.model.commands.UpdateInvoiceCommand;
import com.nistra.demy.platform.billing.domain.model.valueobjects.InvoiceStatus;
import com.nistra.demy.platform.billing.domain.model.valueobjects.InvoiceType;
import com.nistra.demy.platform.billing.interfaces.rest.resources.UpdateInvoiceResource;
import com.nistra.demy.platform.shared.domain.model.valueobjects.Money;

import java.math.BigDecimal;
import java.util.Currency;

public class UpdateInvoiceCommandFromResourceAssembler {

    public static UpdateInvoiceCommand toCommand(Long billingAccountId, Long invoiceId, UpdateInvoiceResource resource) {
        return new UpdateInvoiceCommand(
                billingAccountId,
                invoiceId,
                InvoiceType.valueOf(resource.invoiceType()),
                new Money(new BigDecimal(resource.amount()), Currency.getInstance(resource.currency())),
                resource.description(),
                InvoiceStatus.valueOf(resource.status())
        );
    }
}