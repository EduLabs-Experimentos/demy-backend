package com.nistra.demy.platform.billing.domain.model.entities;

import com.nistra.demy.platform.billing.domain.model.aggregates.BillingAccount;
import com.nistra.demy.platform.billing.domain.model.commands.UpdateInvoiceCommand;
import com.nistra.demy.platform.billing.domain.model.valueobjects.InvoiceStatus;
import com.nistra.demy.platform.billing.domain.model.valueobjects.InvoiceType;
import com.nistra.demy.platform.shared.domain.model.entities.AuditableModel;
import com.nistra.demy.platform.shared.domain.model.valueobjects.Money;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Entity
public class Invoice extends AuditableModel {

    @Enumerated(EnumType.STRING)
    private InvoiceType invoiceType;

    @Embedded
    private Money amount;

    private String description;

    private LocalDate issueDate;

    private LocalDate dueDate;

    @Setter
    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;

    @ManyToOne
    @JoinColumn(name = "billing_account_id")
    private BillingAccount billingAccount;

    protected Invoice() {
    }

    protected void setBillingAccount(BillingAccount billingAccount) {
        this.billingAccount = billingAccount;
    }

    public Invoice(
            InvoiceType invoiceType,
            Money amount,
            String description,
            LocalDate issueDate,
            LocalDate dueDate,
            InvoiceStatus status,
            BillingAccount billingAccount
    ) {
        this.invoiceType = invoiceType;
        this.amount = amount;
        this.description = description;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.status = status;
        this.billingAccount = billingAccount;
    }

    public void setInvoiceType(InvoiceType invoiceType) {
    }

    public void setAmount(Money amount) {
    }

    public void setDescription(String description) {
    }

    public void updateDetails(UpdateInvoiceCommand command) {
        if (this.status != InvoiceStatus.PENDING)
            throw new IllegalStateException("Only pending invoices can be updated");
        if (command.invoiceType() != null)
            this.invoiceType = command.invoiceType();
        if (command.amount() != null)
            this.amount = command.amount();
        if (command.description() != null && !command.description().isBlank())
            this.description = command.description();
        if (command.status() != null)
            this.status = command.status();
    }
}
