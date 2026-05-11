package com.nistra.demy.platform.billing.domain.services;

import com.nistra.demy.platform.billing.domain.model.aggregates.BillingAccount;
import com.nistra.demy.platform.billing.domain.model.commands.*;
import com.nistra.demy.platform.billing.domain.model.entities.Invoice;

import java.util.Optional;

public interface BillingAccountCommandService {
    Optional<BillingAccount> handle(CreateBillingAccountCommand command);

    Optional<BillingAccount> handle(AssignInvoiceToBillingAccountCommand command);

    Optional<Invoice> handle(MarkInvoiceAsPaidCommand command);

    Optional<Invoice> handle(UpdateInvoiceCommand command);

    void handle(DeleteInvoiceCommand command);
}
