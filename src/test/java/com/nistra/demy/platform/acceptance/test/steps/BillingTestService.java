package com.nistra.demy.platform.acceptance.test.steps;


import com.nistra.demy.platform.billing.domain.model.aggregates.BillingAccount;
import com.nistra.demy.platform.billing.domain.model.commands.AssignInvoiceToBillingAccountCommand;
import com.nistra.demy.platform.billing.domain.model.commands.CreateBillingAccountCommand;
import com.nistra.demy.platform.billing.domain.model.valueobjects.InvoiceStatus;
import com.nistra.demy.platform.billing.infrastructure.persistence.jpa.repositories.BillingAccountRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BillingTestService {

    @PersistenceContext
    private EntityManager entityManager;

    private final BillingAccountRepository billingAccountRepository;

    public BillingTestService(BillingAccountRepository billingAccountRepository) {
        this.billingAccountRepository = billingAccountRepository;
    }

    @Transactional
    public BillingAccount createAccount(CreateBillingAccountCommand cmd,
                                        com.nistra.demy.platform.shared.domain.model.valueobjects.AcademyId academyId) {
        BillingAccount account = new BillingAccount(cmd, academyId);
        billingAccountRepository.save(account);
        entityManager.flush();
        return reloadWithInvoices(account.getId());
    }

    @Transactional
    public BillingAccount assignPendingInvoice(Long accountId, AssignInvoiceToBillingAccountCommand cmd) {
        BillingAccount account = reloadWithInvoices(accountId);
        account.assignInvoice(cmd);
        billingAccountRepository.save(account);
        entityManager.flush();
        entityManager.clear();
        return reloadWithInvoices(accountId);
    }

    @Transactional
    public BillingAccount markInvoiceAsPaidDirectly(Long accountId, Long invoiceId) {
        // Bypass InvoicePaidEventHandler (requires Tenant ID not present in test context)
        entityManager.createQuery(
                        "UPDATE Invoice i SET i.status = :status WHERE i.id = :id"
                )
                .setParameter("status", InvoiceStatus.PAID)
                .setParameter("id", invoiceId)
                .executeUpdate();
        entityManager.flush();
        entityManager.clear();
        return reloadWithInvoices(accountId);
    }

    @Transactional
    public BillingAccount deleteInvoice(Long accountId, Long invoiceId) {
        BillingAccount account = reloadWithInvoices(accountId);
        account.deleteInvoice(invoiceId);
        billingAccountRepository.save(account);
        entityManager.flush();
        entityManager.clear();
        return reloadWithInvoices(accountId);
    }

    @Transactional
    public BillingAccount assignInvoice(Long accountId, AssignInvoiceToBillingAccountCommand cmd) {
        BillingAccount account = reloadWithInvoices(accountId);
        account.assignInvoice(cmd);
        billingAccountRepository.save(account);
        entityManager.flush();
        entityManager.clear();
        return reloadWithInvoices(accountId);
    }

    @Transactional(readOnly = true)
    public BillingAccount reloadWithInvoices(Long accountId) {
        return entityManager.createQuery(
                        "SELECT ba FROM BillingAccount ba " +
                                "LEFT JOIN FETCH ba.invoices " +
                                "WHERE ba.id = :id",
                        BillingAccount.class)
                .setParameter("id", accountId)
                .getSingleResult();
    }

    @Transactional
    public void deleteAll() {
        billingAccountRepository.deleteAll();
    }
}