package com.nistra.demy.platform.accountingfinance.application.internal.queryservices;

import com.nistra.demy.platform.accountingfinance.application.internal.outboundservices.acl.ExternalIamService;
import com.nistra.demy.platform.accountingfinance.domain.model.aggregates.Transaction;
import com.nistra.demy.platform.accountingfinance.domain.model.queries.GetAllTransactionsQuery;
import com.nistra.demy.platform.accountingfinance.domain.model.queries.GetTransactionByIdQuery;
import com.nistra.demy.platform.accountingfinance.domain.services.TransactionQueryService;
import com.nistra.demy.platform.accountingfinance.infrastructure.persistence.jpa.repositories.TransactionRepository;
import com.nistra.demy.platform.accountingfinance.infrastructure.persistence.jpa.specifications.TransactionSpecificationsBuilder;
import com.nistra.demy.platform.shared.domain.model.valueobjects.AcademyId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionQueryServiceImpl implements TransactionQueryService {

    private final TransactionRepository transactionRepository;
    private final ExternalIamService externalIamService;

    public TransactionQueryServiceImpl(
            TransactionRepository transactionRepository,
            ExternalIamService externalIamService
    ) {
        this.transactionRepository = transactionRepository;
        this.externalIamService = externalIamService;
    }

    @Override
    public Optional<Transaction> handle(GetTransactionByIdQuery query) {
        var academyId = externalIamService.fetchCurrentAcademyId()
                .orElseThrow(() -> new RuntimeException("No academy found for the current user"));
        var transaction = transactionRepository.findById(query.transactionId());
        if (transaction.isPresent() && !transaction.get().getAcademyId().equals(new AcademyId(academyId.academyId()))) {
            throw new RuntimeException("Transaction does not belong to the current academy");
        }
        return transaction;
    }

    @Override
    public List<Transaction> handle(GetAllTransactionsQuery query) {
        var academyId = externalIamService.fetchCurrentAcademyId()
                .orElseThrow(() -> new RuntimeException("No academy found for the current user"));
        var specification = TransactionSpecificationsBuilder.build(
                academyId,
                query.type(),
                query.category(),
                query.method()
        );
        return transactionRepository.findAll(specification);
    }
}
