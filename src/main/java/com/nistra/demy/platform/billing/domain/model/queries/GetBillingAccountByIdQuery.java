package com.nistra.demy.platform.billing.domain.model.queries;

public record GetBillingAccountByIdQuery(
        Long billingAccountId
) {
    public GetBillingAccountByIdQuery {
        if (billingAccountId == null || billingAccountId <= 0)
            throw new IllegalArgumentException("Billing Account ID must be a positive non-null value");
    }
}
