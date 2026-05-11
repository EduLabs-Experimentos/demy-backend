package com.nistra.demy.platform.billing.domain.model.aggregates;

import com.nistra.demy.platform.billing.domain.model.commands.AssignInvoiceToBillingAccountCommand;
import com.nistra.demy.platform.billing.domain.model.commands.CreateBillingAccountCommand;
import com.nistra.demy.platform.billing.domain.model.commands.UpdateInvoiceCommand;
import com.nistra.demy.platform.billing.domain.model.entities.Invoice;
import com.nistra.demy.platform.billing.domain.model.events.InvoicePaidEvent;
import com.nistra.demy.platform.billing.domain.model.valueobjects.AccountStatus;
import com.nistra.demy.platform.billing.domain.model.valueobjects.InvoiceStatus;
import com.nistra.demy.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.nistra.demy.platform.shared.domain.model.valueobjects.AcademyId;
import com.nistra.demy.platform.shared.domain.model.valueobjects.DniNumber;
import com.nistra.demy.platform.shared.domain.model.valueobjects.StudentId;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.*;

@Entity
public class BillingAccount extends AuditableAbstractAggregateRoot<BillingAccount> {

    @Embedded
    @Getter
    private StudentId studentId;

    @Embedded
    @Getter
    private DniNumber dniNumber;

    @OneToMany(mappedBy = "billingAccount", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Invoice> invoices;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @Embedded
    @Getter
    private AcademyId academyId;

    protected BillingAccount() {}

    public BillingAccount(CreateBillingAccountCommand command, AcademyId academyId) {
        this.studentId = command.studentId();
        this.dniNumber = command.dniNumber();
        this.invoices = new ArrayList<>();
        this.status = AccountStatus.ACTIVE;
        this.academyId = academyId;
    }

    public void assignInvoice(AssignInvoiceToBillingAccountCommand command) {
        var invoice = new Invoice(
                command.invoiceType(),
                command.amount(),
                command.description(),
                command.issueDate(),
                command.dueDate(),
                command.status(),
                this
        );
        invoices.add(invoice);
    }

    public void markInvoiceAsPaid(Long invoiceId) {
        var invoice = invoices.stream()
                .filter(i -> i.getId().equals(invoiceId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
        invoice.setStatus(InvoiceStatus.PAID);
        addDomainEvent(new InvoicePaidEvent(
                this,
                invoice.getInvoiceType(),
                invoice.getAmount(),
                invoice.getDescription()
        ));
    }

    public Invoice findInvoiceById(Long invoiceId) {
        return this.invoices.stream()
                .filter(i -> Objects.equals(i.getId(), invoiceId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
    }

    public List<Invoice> getInvoices() {
        return Collections.unmodifiableList(invoices);
    }

    public Invoice updateInvoice(UpdateInvoiceCommand command) {
        var invoice = this.invoices.stream()
                .filter(i -> i.getId().equals(command.invoiceId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
        invoice.updateDetails(command);
        return invoice;
    }

    public void deleteInvoice(Long invoiceId) {
        var invoice = invoices.stream()
                .filter(i -> i.getId().equals(invoiceId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
        if (invoice.getStatus() == InvoiceStatus.PAID)
            throw new IllegalStateException("Cannot delete a paid invoice.");
        invoices.remove(invoice);
    }
}
