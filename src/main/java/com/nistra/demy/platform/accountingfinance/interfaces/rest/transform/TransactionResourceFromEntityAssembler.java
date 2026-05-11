package com.nistra.demy.platform.accountingfinance.interfaces.rest.transform;

import com.nistra.demy.platform.accountingfinance.domain.model.aggregates.Transaction;
import com.nistra.demy.platform.accountingfinance.interfaces.rest.resources.TransactionResource;

import java.util.Collection;
import java.util.List;

public class TransactionResourceFromEntityAssembler {
    public static TransactionResource toResourceFromEntity(Transaction entity) {
        return new TransactionResource(
                entity.getId(),
                entity.getTransactionType().toString(),
                entity.getTransactionCategory().toString(),
                entity.getTransactionMethod().toString(),
                entity.getAmount().amount(),
                entity.getAmount().currency().getCurrencyCode(),
                entity.getDescription(),
                entity.getTransactionDate().toString()
        );
    }

    public static List<TransactionResource> toResourcesFromEntities(Collection<Transaction> entities) {
        return entities.stream()
                .map(TransactionResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
    }
}
