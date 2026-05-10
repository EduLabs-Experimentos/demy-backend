package com.nistra.demy.platform.accountingfinance.application.internal.commandservices;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para TransactionCommandServiceImpl usando Mockito.
 * Valida los casos principales de las historias US025, US026 y US027 del bounded context Finance.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TransactionCommandServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private ExternalIamService externalIamService;

    @InjectMocks
    private TransactionCommandServiceImpl transactionCommandService;

    private static final Long TRANSACTION_ID = 1L;
    private static final Long ACADEMY_ID = 5L;

    private AcademyId academyId;
    private Money incomeAmount;
    private Money expenseAmount;

    private RegisterTransactionCommand registerCommand;
    private UpdateTransactionCommand updateCommand;
    private DeleteTransactionCommand deleteCommand;

    private Transaction transaction;

    @BeforeEach
    void setUp() {
        academyId = new AcademyId(ACADEMY_ID);
        incomeAmount = new Money(new BigDecimal("150.00"), Currency.getInstance("PEN"));
        expenseAmount = new Money(new BigDecimal("80.00"), Currency.getInstance("PEN"));

        registerCommand = new RegisterTransactionCommand(
                "income",
                "student enrollment",
                "bank transfer",
                incomeAmount,
                "Pago de matricula",
                LocalDate.of(2026, 5, 9)
        );

        updateCommand = new UpdateTransactionCommand(
                TRANSACTION_ID,
                "expense",
                "office supplies",
                "debit card",
                expenseAmount,
                "Compra de utiles de oficina",
                LocalDate.of(2026, 5, 10)
        );

        deleteCommand = new DeleteTransactionCommand(TRANSACTION_ID);
        transaction = new Transaction(registerCommand, academyId);
    }

    @Test
    @DisplayName("US025 - Registrar ingreso/egreso exitosamente guarda la transaccion")
    void handle_RegisterTransaction_Success_SavesTransaction() {
        // Arrange
        when(externalIamService.fetchCurrentAcademyId()).thenReturn(Optional.of(academyId));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Optional<Transaction> result = transactionCommandService.handle(registerCommand);

        // Assert
        assertThat(result)
                .as("El servicio debe retornar una transaccion registrada cuando los datos contables son validos")
                .isPresent();
        assertThat(result.get().getTransactionType())
                .as("La transaccion registrada debe conservar el tipo INCOME")
                .isEqualTo(TransactionType.INCOME);
        assertThat(result.get().getTransactionCategory())
                .as("La transaccion registrada debe conservar la categoria STUDENT_ENROLLMENT")
                .isEqualTo(TransactionCategory.STUDENT_ENROLLMENT);
        assertThat(result.get().getTransactionMethod())
                .as("La transaccion registrada debe conservar el metodo BANK_TRANSFER")
                .isEqualTo(TransactionMethod.BANK_TRANSFER);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    @DisplayName("US025 - Registrar transaccion sin academia actual lanza RuntimeException")
    void handle_RegisterTransaction_NoAcademy_ThrowsRuntimeException() {
        // Arrange
        when(externalIamService.fetchCurrentAcademyId()).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> transactionCommandService.handle(registerCommand))
                .as("El servicio debe rechazar el registro si no existe una academia actual")
                .isInstanceOf(RuntimeException.class)
                .hasMessage("No academy found");

        verify(transactionRepository, never()).save(any());
    }

    @Test
    @DisplayName("US026 - Actualizar ingreso/egreso existente retorna transaccion actualizada")
    void handle_UpdateTransaction_Success_ReturnsUpdatedTransaction() {
        // Arrange
        when(externalIamService.fetchCurrentAcademyId()).thenReturn(Optional.of(academyId));
        when(transactionRepository.findById(TRANSACTION_ID)).thenReturn(Optional.of(transaction));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Optional<Transaction> result = transactionCommandService.handle(updateCommand);

        // Assert
        assertThat(result)
                .as("El servicio debe retornar la transaccion actualizada cuando pertenece a la academia actual")
                .isPresent();
        assertThat(result.get().getTransactionType())
                .as("La actualizacion debe cambiar el tipo de transaccion a EXPENSE")
                .isEqualTo(TransactionType.EXPENSE);
        assertThat(result.get().getTransactionCategory())
                .as("La actualizacion debe cambiar la categoria a OFFICE_SUPPLIES")
                .isEqualTo(TransactionCategory.OFFICE_SUPPLIES);
        assertThat(result.get().getTransactionMethod())
                .as("La actualizacion debe cambiar el metodo a DEBIT_CARD")
                .isEqualTo(TransactionMethod.DEBIT_CARD);
        assertThat(result.get().getAmount())
                .as("La actualizacion debe cambiar el monto de la transaccion")
                .isEqualTo(expenseAmount);
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    @DisplayName("US026 - Actualizar transaccion inexistente lanza RuntimeException")
    void handle_UpdateTransaction_NotFound_ThrowsRuntimeException() {
        // Arrange
        when(externalIamService.fetchCurrentAcademyId()).thenReturn(Optional.of(academyId));
        when(transactionRepository.findById(TRANSACTION_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> transactionCommandService.handle(updateCommand))
                .as("El servicio debe rechazar la actualizacion si la transaccion no existe")
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Transaction not found with id: 1");

        verify(transactionRepository, never()).save(any());
    }

    @Test
    @DisplayName("US026 - Actualizar transaccion de otra academia lanza RuntimeException")
    void handle_UpdateTransaction_DifferentAcademy_ThrowsRuntimeException() {
        // Arrange
        AcademyId otherAcademy = new AcademyId(99L);
        when(externalIamService.fetchCurrentAcademyId()).thenReturn(Optional.of(otherAcademy));
        when(transactionRepository.findById(TRANSACTION_ID)).thenReturn(Optional.of(transaction));

        // Act & Assert
        assertThatThrownBy(() -> transactionCommandService.handle(updateCommand))
                .as("El servicio debe impedir actualizar una transaccion de otra academia")
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Transaction does not belong to the current academy");

        verify(transactionRepository, never()).save(any());
    }

    @Test
    @DisplayName("US027 - Eliminar ingreso/egreso existente llama delete")
    void handle_DeleteTransaction_Success_CallsDelete() {
        // Arrange
        when(externalIamService.fetchCurrentAcademyId()).thenReturn(Optional.of(academyId));
        when(transactionRepository.findById(TRANSACTION_ID)).thenReturn(Optional.of(transaction));

        // Act
        transactionCommandService.handle(deleteCommand);

        // Assert
        verify(transactionRepository, times(1)).delete(transaction);
    }
}
