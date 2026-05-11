package com.nistra.demy.platform.accountingfinance.application.internal.eventhandlers;

import com.nistra.demy.platform.accountingfinance.domain.model.commands.RegisterTransactionCommand;
import com.nistra.demy.platform.accountingfinance.domain.model.valueobjects.TransactionMethod;
import com.nistra.demy.platform.accountingfinance.domain.model.valueobjects.TransactionType;
import com.nistra.demy.platform.accountingfinance.domain.services.TransactionCommandService;
import com.nistra.demy.platform.billing.domain.model.events.InvoicePaidEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import static com.nistra.demy.platform.accountingfinance.domain.model.valueobjects.TransactionCategory.fromString;

@Service
public class InvoicePaidEventHandler {

    public final TransactionCommandService transactionCommandService;

    public InvoicePaidEventHandler(TransactionCommandService transactionCommandService) {
        this.transactionCommandService = transactionCommandService;
    }

    @EventListener
    public void on(InvoicePaidEvent event) {
        var registerTransactionCommand = new RegisterTransactionCommand(
                TransactionType.INCOME.name(),
                fromString(event.getInvoiceType().name()).name(),
                TransactionMethod.CASH.name(),
                event.getAmount(),
                event.getDescription(),
                event.getPaidDate()
        );
        try {
            transactionCommandService.handle(registerTransactionCommand);
        } catch (Exception e) {
            throw new RuntimeException("Failed to register transaction for paid invoice: %s".formatted(e.getMessage()));
        }
    }
}
