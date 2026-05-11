package com.nistra.demy.platform.billing.interfaces.rest.transform;

import com.nistra.demy.platform.billing.domain.model.entities.Invoice;
import com.nistra.demy.platform.billing.interfaces.rest.resources.InvoiceResource;

import java.util.Collection;
import java.util.List;

public class InvoiceResourceFromEntityAssembler {
    public static InvoiceResource toResourceFromEntity(Invoice entity) {
        return new InvoiceResource(
                entity.getId(),
                entity.getInvoiceType().name(),
                entity.getAmount().amount().toPlainString(),
                entity.getAmount().currency().getCurrencyCode(),
                entity.getDescription(),
                entity.getIssueDate().toString(),
                entity.getDueDate().toString(),
                entity.getStatus().name()
        );
    }

    public static List<InvoiceResource> toResourcesFromEntities(Collection<Invoice> entities) {
        return entities.stream()
                .map(InvoiceResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
    }
}
