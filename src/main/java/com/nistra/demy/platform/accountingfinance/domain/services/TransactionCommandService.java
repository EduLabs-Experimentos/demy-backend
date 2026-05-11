package com.nistra.demy.platform.accountingfinance.domain.services;

import com.nistra.demy.platform.accountingfinance.domain.model.aggregates.Transaction;
import com.nistra.demy.platform.accountingfinance.domain.model.commands.DeleteTransactionCommand;
import com.nistra.demy.platform.accountingfinance.domain.model.commands.RegisterTransactionCommand;
import com.nistra.demy.platform.accountingfinance.domain.model.commands.UpdateTransactionCommand;

import java.util.Optional;

public interface TransactionCommandService {
    Optional<Transaction> handle(RegisterTransactionCommand command);

    Optional<Transaction> handle(UpdateTransactionCommand command);

    void handle(DeleteTransactionCommand command);
}
