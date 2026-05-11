package com.nistra.demy.platform.billing.application.internal.queryservices;

import com.nistra.demy.platform.billing.application.internal.outboundservices.acl.ExternalEnrollmentService;
import com.nistra.demy.platform.billing.application.internal.outboundservices.acl.ExternalIamService;
import com.nistra.demy.platform.billing.domain.model.aggregates.BillingAccount;
import com.nistra.demy.platform.billing.domain.model.entities.Invoice;
import com.nistra.demy.platform.billing.domain.model.queries.*;
import com.nistra.demy.platform.billing.domain.services.BillingAccountQueryService;
import com.nistra.demy.platform.billing.infrastructure.persistence.jpa.repositories.BillingAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BillingAccountQueryServiceImpl implements BillingAccountQueryService {

    private final BillingAccountRepository billingAccountRepository;
    private final ExternalIamService externalIamService;
    private final ExternalEnrollmentService externalEnrollmentService;

    public BillingAccountQueryServiceImpl(
            BillingAccountRepository billingAccountRepository,
            ExternalIamService externalIamService,
            ExternalEnrollmentService externalEnrollmentService
    ) {
        this.billingAccountRepository = billingAccountRepository;
        this.externalIamService = externalIamService;
        this.externalEnrollmentService = externalEnrollmentService;
    }

    @Override
    public Optional<BillingAccount> handle(GetBillingAccountByIdQuery query) {
        return billingAccountRepository.findById(query.billingAccountId());
    }

    @Override
    public List<BillingAccount> handle(GetAllBillingAccountsQuery query) {
        var academyId = externalIamService.fetchCurrentAcademyId()
                .orElseThrow(() -> new RuntimeException("No academy found for the current user"));
        return billingAccountRepository.findAllByAcademyId(academyId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Invoice> handle(GetAllInvoicesByBillingAccountIdQuery query) {
        var billingAccount = billingAccountRepository.findById(query.billingAccountId())
                .orElseThrow(() -> new RuntimeException("Billing account not found for id: %s".formatted(query.billingAccountId())));
        return billingAccount.getInvoices();
    }

    @Override
    public List<Invoice> handle(GetAllInvoicesByStudentIdQuery query) {
        var billingAccount = billingAccountRepository.findByStudentId(query.studentId())
                .orElseThrow(() -> new RuntimeException("Billing account not found for student id: %s".formatted(query.studentId())));
        return billingAccount.getInvoices();
    }

    @Override
    public List<Invoice> handle(GetAllInvoicesByStudentDniNumberQuery query) {
        var studentId = externalEnrollmentService.fetchStudentInvoiceIdByDniNumber(query.dniNumber().dniNumber())
                .orElseThrow(() -> new RuntimeException("Student not found for DNI number: %s".formatted(query.dniNumber().dniNumber())));
        var billingAccount = billingAccountRepository.findByStudentId(studentId)
                .orElseThrow(() -> new RuntimeException("Billing account not found for student id: %s".formatted(studentId.studentId())));
        return billingAccount.getInvoices();
    }
}
