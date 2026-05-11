package com.nistra.demy.platform.billing.interfaces.rest.transform;

import com.nistra.demy.platform.billing.domain.model.commands.AssignInvoiceToBillingAccountCommand;
import com.nistra.demy.platform.billing.domain.model.valueobjects.InvoiceStatus;
import com.nistra.demy.platform.billing.domain.model.valueobjects.InvoiceType;
import com.nistra.demy.platform.billing.interfaces.rest.resources.AssignInvoiceResource;
import com.nistra.demy.platform.shared.domain.model.valueobjects.Money;

import java.math.BigDecimal;
import java.util.Currency;

public class AssignInvoiceCommandFromResourceAssembler {
    public static AssignInvoiceToBillingAccountCommand toCommandFromResource(Long billingAccountId, AssignInvoiceResource resource) {
        return new AssignInvoiceToBillingAccountCommand(
                InvoiceType.valueOf(resource.invoiceType()),
                new Money(new BigDecimal(resource.amount()), Currency.getInstance(resource.currency())),
                resource.description(),
                resource.issueDate(),
                resource.dueDate(),
                InvoiceStatus.valueOf(resource.status()),
                billingAccountId
        );
    }
}
