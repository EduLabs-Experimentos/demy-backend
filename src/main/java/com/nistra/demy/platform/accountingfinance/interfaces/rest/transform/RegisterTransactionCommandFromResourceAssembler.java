package com.nistra.demy.platform.accountingfinance.interfaces.rest.transform;

import com.nistra.demy.platform.accountingfinance.domain.model.commands.RegisterTransactionCommand;
import com.nistra.demy.platform.accountingfinance.interfaces.rest.resources.RegisterTransactionResource;
import com.nistra.demy.platform.shared.domain.model.valueobjects.Money;

import java.util.Currency;

public class RegisterTransactionCommandFromResourceAssembler {
    public static RegisterTransactionCommand toCommandFromResource(RegisterTransactionResource resource) {
        return new RegisterTransactionCommand(
                resource.transactionType(),
                resource.transactionCategory(),
                resource.transactionMethod(),
                new Money(resource.amount(), Currency.getInstance(resource.currency())),
                resource.description(),
                resource.transactionDate()
        );
    }
}
