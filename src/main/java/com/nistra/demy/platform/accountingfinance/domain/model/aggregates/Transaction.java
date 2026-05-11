package com.nistra.demy.platform.accountingfinance.domain.model.aggregates;

import com.nistra.demy.platform.accountingfinance.domain.model.commands.RegisterTransactionCommand;
import com.nistra.demy.platform.accountingfinance.domain.model.commands.UpdateTransactionCommand;
import com.nistra.demy.platform.accountingfinance.domain.model.valueobjects.TransactionCategory;
import com.nistra.demy.platform.accountingfinance.domain.model.valueobjects.TransactionMethod;
import com.nistra.demy.platform.accountingfinance.domain.model.valueobjects.TransactionType;
import com.nistra.demy.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.nistra.demy.platform.shared.domain.model.valueobjects.AcademyId;
import com.nistra.demy.platform.shared.domain.model.valueobjects.Money;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

import java.time.LocalDate;

@Entity
public class Transaction extends AuditableAbstractAggregateRoot<Transaction> {

    @Getter
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Getter
    @Enumerated(EnumType.STRING)
    private TransactionCategory transactionCategory;

    @Getter
    @Enumerated(EnumType.STRING)
    private TransactionMethod transactionMethod;

    @Embedded
    @Getter
    private Money amount;

    @Getter
    private String description;

    @Getter
    private LocalDate transactionDate;

    @Embedded
    @Getter
    private AcademyId academyId;

    protected Transaction() {}

    public Transaction(RegisterTransactionCommand command, AcademyId academyId) {
        this.transactionType = TransactionType.fromString(command.transactionType());
        this.transactionCategory = TransactionCategory.fromString(command.transactionCategory());
        this.transactionMethod = TransactionMethod.fromString(command.transactionMethod());
        this.amount = command.amount();
        this.description = command.description();
        this.transactionDate = command.transactionDate();
        this.academyId = academyId;
    }

    public void updateTransaction(UpdateTransactionCommand command) {
        this.transactionType = TransactionType.fromString(command.transactionType());
        this.transactionCategory = TransactionCategory.fromString(command.transactionCategory());
        this.transactionMethod = TransactionMethod.fromString(command.transactionMethod());
        this.amount = command.amount();
        this.description = command.description();
        this.transactionDate = command.transactionDate();
    }
}
