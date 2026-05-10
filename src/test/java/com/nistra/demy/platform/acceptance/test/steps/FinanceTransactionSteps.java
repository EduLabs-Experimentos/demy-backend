package com.nistra.demy.platform.acceptance.test.steps;

import com.nistra.demy.platform.accountingfinance.application.internal.commandservices.TransactionCommandServiceImpl;
import com.nistra.demy.platform.accountingfinance.application.internal.outboundservices.acl.ExternalIamService;
import com.nistra.demy.platform.accountingfinance.domain.model.aggregates.Transaction;
import com.nistra.demy.platform.accountingfinance.domain.model.commands.DeleteTransactionCommand;
import com.nistra.demy.platform.accountingfinance.domain.model.commands.RegisterTransactionCommand;
import com.nistra.demy.platform.accountingfinance.domain.model.commands.UpdateTransactionCommand;
import com.nistra.demy.platform.accountingfinance.domain.model.valueobjects.TransactionCategory;
import com.nistra.demy.platform.accountingfinance.domain.model.valueobjects.TransactionMethod;
import com.nistra.demy.platform.accountingfinance.domain.model.valueobjects.TransactionType;
import com.nistra.demy.platform.accountingfinance.infrastructure.persistence.jpa.repositories.TransactionRepository;
import com.nistra.demy.platform.shared.domain.model.valueobjects.AcademyId;
import com.nistra.demy.platform.shared.domain.model.valueobjects.Money;
import io.cucumber.java.en.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FinanceTransactionSteps {

    private static final Long TRANSACTION_ID = 1L;

    private AcademyId academyId;
    private TransactionRepository transactionRepository;
    private ExternalIamService externalIamService;
    private TransactionCommandServiceImpl transactionCommandService;

    private Transaction transaction;
    private Exception exception;
    private String message;

    @Given("existe una academia financiera con id {long}")
    public void existe_una_academia_financiera_con_id(Long id) {
        this.academyId = new AcademyId(id);
        this.transactionRepository = mock(TransactionRepository.class);
        this.externalIamService = mock(ExternalIamService.class);
        this.transactionCommandService = new TransactionCommandServiceImpl(transactionRepository, externalIamService);
        this.exception = null;
        this.message = null;

        when(externalIamService.fetchCurrentAcademyId()).thenReturn(Optional.of(academyId));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(inv -> inv.getArgument(0));
    }

    @Given("existe una transaccion financiera registrada con id {long}")
    public void existe_una_transaccion_financiera_registrada_con_id(Long id) {
        RegisterTransactionCommand command = new RegisterTransactionCommand(
                "income",
                "student enrollment",
                "cash",
                new Money(new BigDecimal("150.00"), Currency.getInstance("PEN")),
                "Pago de matricula",
                LocalDate.of(2026, 5, 9)
        );
        this.transaction = new Transaction(command, academyId);
        when(transactionRepository.findById(id)).thenReturn(Optional.of(transaction));
    }

    @When("registro una transaccion financiera con tipo {string}, categoria {string}, metodo {string}, monto {bigDecimal} y moneda {word}")
    public void registro_una_transaccion_financiera(String type, String category, String method, BigDecimal amount, String currencyCode) {
        try {
            RegisterTransactionCommand command = new RegisterTransactionCommand(
                    type,
                    category,
                    method,
                    new Money(amount, Currency.getInstance(currencyCode)),
                    "Movimiento financiero",
                    LocalDate.of(2026, 5, 9)
            );
            this.transaction = transactionCommandService.handle(command).orElseThrow();
            this.message = "Test Passed";
        } catch (Exception e) {
            this.exception = e;
            this.message = "Error";
        }
    }

    @When("actualizo la transaccion financiera con tipo {string}, categoria {string}, metodo {string}, monto {bigDecimal} y moneda {word}")
    public void actualizo_la_transaccion_financiera(String type, String category, String method, BigDecimal amount, String currencyCode) {
        try {
            UpdateTransactionCommand command = new UpdateTransactionCommand(
                    TRANSACTION_ID,
                    type,
                    category,
                    method,
                    new Money(amount, Currency.getInstance(currencyCode)),
                    "Movimiento financiero actualizado",
                    LocalDate.of(2026, 5, 10)
            );
            this.transaction = transactionCommandService.handle(command).orElseThrow();
            this.message = "Test Passed";
        } catch (Exception e) {
            this.exception = e;
            this.message = "Error";
        }
    }

    @When("elimino la transaccion financiera")
    public void elimino_la_transaccion_financiera() {
        try {
            transactionCommandService.handle(new DeleteTransactionCommand(TRANSACTION_ID));
            this.message = "Test Passed";
        } catch (Exception e) {
            this.exception = e;
            this.message = "Error";
        }
    }

    @Then("debe crearse una transaccion con tipo {string}, categoria {string}, metodo {string}, monto {bigDecimal} y moneda {string}")
    public void debe_crearse_una_transaccion(String type, String category, String method, BigDecimal amount, String currencyCode) {
        if (exception != null) {
            return;
        }

        assertNotNull(transaction, "La transaccion financiera debe crearse correctamente");
        assertEquals(TransactionType.valueOf(type), transaction.getTransactionType(),
                "El tipo de transaccion debe coincidir con el valor esperado");
        assertEquals(TransactionCategory.valueOf(category), transaction.getTransactionCategory(),
                "La categoria de transaccion debe coincidir con el valor esperado");
        assertEquals(TransactionMethod.valueOf(method), transaction.getTransactionMethod(),
                "El metodo de transaccion debe coincidir con el valor esperado");
        assertEquals(amount, transaction.getAmount().amount(),
                "El monto de la transaccion debe coincidir con el valor esperado");
        assertEquals(currencyCode, transaction.getAmount().currency().getCurrencyCode(),
                "La moneda de la transaccion debe coincidir con el valor esperado");
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Then("debe actualizarse la transaccion con tipo {string}, categoria {string}, metodo {string}, monto {bigDecimal} y moneda {string}")
    public void debe_actualizarse_la_transaccion(String type, String category, String method, BigDecimal amount, String currencyCode) {
        if (exception != null) {
            return;
        }

        assertNotNull(transaction, "La transaccion financiera debe existir para ser actualizada");
        assertEquals(TransactionType.valueOf(type), transaction.getTransactionType(),
                "El tipo actualizado debe coincidir con el valor esperado");
        assertEquals(TransactionCategory.valueOf(category), transaction.getTransactionCategory(),
                "La categoria actualizada debe coincidir con el valor esperado");
        assertEquals(TransactionMethod.valueOf(method), transaction.getTransactionMethod(),
                "El metodo actualizado debe coincidir con el valor esperado");
        assertEquals(amount, transaction.getAmount().amount(),
                "El monto actualizado debe coincidir con el valor esperado");
        assertEquals(currencyCode, transaction.getAmount().currency().getCurrencyCode(),
                "La moneda actualizada debe coincidir con el valor esperado");
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Then("la transaccion financiera debe eliminarse correctamente")
    public void la_transaccion_financiera_debe_eliminarse_correctamente() {
        if (exception != null) {
            return;
        }

        verify(transactionRepository, times(1)).delete(any(Transaction.class));
    }

    @Then("el mensaje final de finance es {string}")
    public void el_mensaje_final_de_finance_es(String expectedMessage) {
        assertEquals(expectedMessage, message, "El mensaje final del escenario de finance debe coincidir");
    }
}
