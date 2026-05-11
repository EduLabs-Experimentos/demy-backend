package com.nistra.demy.platform.billing.domain.model.events;

import com.nistra.demy.platform.billing.domain.model.valueobjects.InvoiceType;
import com.nistra.demy.platform.shared.domain.model.valueobjects.Money;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDate;

@Getter
public class InvoicePaidEvent extends ApplicationEvent {

    InvoiceType invoiceType;

    Money amount;

    String description;

    LocalDate paidDate;

    public InvoicePaidEvent(Object source, InvoiceType invoiceType, Money amount, String description) {
        super(source);
        this.invoiceType = invoiceType;
        this.amount = amount;
        this.description = description;
        this.paidDate = LocalDate.now();
    }
}
