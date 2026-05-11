package com.nistra.demy.platform.billing.domain.services;

import com.nistra.demy.platform.billing.domain.model.aggregates.BillingAccount;
import com.nistra.demy.platform.billing.domain.model.entities.Invoice;
import com.nistra.demy.platform.billing.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

public interface BillingAccountQueryService {
    Optional<BillingAccount> handle(GetBillingAccountByIdQuery query);

    List<BillingAccount> handle(GetAllBillingAccountsQuery query);

    List<Invoice> handle(GetAllInvoicesByBillingAccountIdQuery query);

    List<Invoice> handle(GetAllInvoicesByStudentIdQuery query);

    List<Invoice> handle(GetAllInvoicesByStudentDniNumberQuery query);
}
