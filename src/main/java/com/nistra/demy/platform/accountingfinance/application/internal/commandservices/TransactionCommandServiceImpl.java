package com.nistra.demy.platform.accountingfinance.application.internal.commandservices;

import com.nistra.demy.platform.accountingfinance.application.internal.outboundservices.acl.ExternalIamService;
import com.nistra.demy.platform.accountingfinance.domain.model.aggregates.Transaction;
import com.nistra.demy.platform.accountingfinance.domain.model.commands.DeleteTransactionCommand;
import com.nistra.demy.platform.accountingfinance.domain.model.commands.RegisterTransactionCommand;
import com.nistra.demy.platform.accountingfinance.domain.model.commands.UpdateTransactionCommand;
import com.nistra.demy.platform.accountingfinance.domain.services.TransactionCommandService;
import com.nistra.demy.platform.accountingfinance.infrastructure.persistence.jpa.repositories.TransactionRepository;
import com.nistra.demy.platform.shared.domain.model.valueobjects.AcademyId;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TransactionCommandServiceImpl implements TransactionCommandService {

    private final TransactionRepository transactionRepository;
    private final ExternalIamService externalIamService;

    public TransactionCommandServiceImpl(
            TransactionRepository transactionRepository,
            ExternalIamService externalIamService
    ) {
        this.transactionRepository = transactionRepository;
        this.externalIamService = externalIamService;
    }

    @Override
    public Optional<Transaction> handle(RegisterTransactionCommand command) {
        var academyId = externalIamService.fetchCurrentAcademyId()
                .orElseThrow(() -> new RuntimeException("No academy found"));
        var transaction = new Transaction(command, academyId);
        try {
            transactionRepository.save(transaction);
            return Optional.of(transaction);
        } catch (Exception e) {
            throw new RuntimeException("Failed to register transaction: %s".formatted(e.getMessage()));
        }
    }

    @Override
    public Optional<Transaction> handle(UpdateTransactionCommand command) {
        var academyId = externalIamService.fetchCurrentAcademyId()
                .orElseThrow(() -> new RuntimeException("No academy found"));
        var transaction = transactionRepository.findById(command.transactionId())
                .orElseThrow(() -> new RuntimeException("Transaction not found with id: %s".formatted(command.transactionId())));
        if (!transaction.getAcademyId().equals(new AcademyId(academyId.academyId()))) {
            throw new RuntimeException("Transaction does not belong to the current academy");
        }
        transaction.updateTransaction(command);
        try {
            transactionRepository.save(transaction);
            return Optional.of(transaction);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update transaction: %s".formatted(e.getMessage()));
        }
    }

    @Override
    public void handle(DeleteTransactionCommand command) {
        var academyId = externalIamService.fetchCurrentAcademyId()
                .orElseThrow(() -> new RuntimeException("No academy found"));
        var transaction = transactionRepository.findById(command.transactionId())
                .orElseThrow(() -> new RuntimeException("Transaction not found with id: %s".formatted(command.transactionId())));
        if (!transaction.getAcademyId().equals(new AcademyId(academyId.academyId()))) {
            throw new RuntimeException("Transaction does not belong to the current academy");
        }
        try {
            transactionRepository.delete(transaction);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete transaction: %s".formatted(e.getMessage()));
        }
    }
}
