package com.nistra.demy.platform.accountingfinance.interfaces.rest.transform;

import com.nistra.demy.platform.accountingfinance.domain.model.commands.UpdateTransactionCommand;
import com.nistra.demy.platform.accountingfinance.interfaces.rest.resources.UpdateTransactionResource;
import com.nistra.demy.platform.shared.domain.model.valueobjects.Money;

import java.util.Currency;

public class UpdateTransactionCommandFromResourceAssembler {
    public static UpdateTransactionCommand toCommandFromResource(Long transactionId, UpdateTransactionResource resource) {
        return new UpdateTransactionCommand(
                transactionId,
                resource.transactionType(),
                resource.transactionCategory(),
                resource.transactionMethod(),
                new Money(resource.amount(), Currency.getInstance(resource.currency())),
                resource.description(),
                resource.transactionDate()
        );
    }
}
