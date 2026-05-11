package com.nistra.demy.platform.billing.application.internal.commandservices;

import com.nistra.demy.platform.billing.application.internal.outboundservices.acl.ExternalIamService;
import com.nistra.demy.platform.billing.domain.model.aggregates.BillingAccount;
import com.nistra.demy.platform.billing.domain.model.commands.AssignInvoiceToBillingAccountCommand;
import com.nistra.demy.platform.billing.domain.model.commands.CreateBillingAccountCommand;
import com.nistra.demy.platform.billing.domain.model.commands.DeleteInvoiceCommand;
import com.nistra.demy.platform.billing.domain.model.commands.MarkInvoiceAsPaidCommand;
import com.nistra.demy.platform.billing.domain.model.entities.Invoice;
import com.nistra.demy.platform.billing.domain.model.valueobjects.InvoiceStatus;
import com.nistra.demy.platform.billing.domain.model.valueobjects.InvoiceType;
import com.nistra.demy.platform.billing.infrastructure.persistence.jpa.repositories.BillingAccountRepository;
import com.nistra.demy.platform.shared.domain.model.valueobjects.AcademyId;
import com.nistra.demy.platform.shared.domain.model.valueobjects.DniNumber;
import com.nistra.demy.platform.shared.domain.model.valueobjects.Money;
import com.nistra.demy.platform.shared.domain.model.valueobjects.StudentId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

/**
 * Unit tests for BillingAccountCommandServiceImpl.
 * Validates business logic and interactions with the repository and IAM service.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("BillingAccountCommandServiceImpl (Mockito)")
class BillingAccountCommandServiceImplTest {

    @Mock
    private BillingAccountRepository billingAccountRepository;

    @Mock
    private ExternalIamService externalIamService;

    @InjectMocks
    private BillingAccountCommandServiceImpl billingAccountService;

    // ── Shared Fixtures ───────────────────────────────────────────────────

    private static final AcademyId ACADEMY_ID = new AcademyId(1L);
    private static final StudentId STUDENT_ID = new StudentId(10L);
    private static final DniNumber DNI = new DniNumber("12345678");
    private static final Money AMOUNT = new Money(new BigDecimal("150.00"), Currency.getInstance("PEN"));

    private BillingAccount newAccount() {
        return new BillingAccount(new CreateBillingAccountCommand(STUDENT_ID, DNI), ACADEMY_ID);
    }

    // ── createBillingAccount ──────────────────────────────────────────────

    @Nested
    @DisplayName("handle(CreateBillingAccountCommand)")
    class CreateBillingAccount {

        @Test
        @DisplayName("should create billing account when academy is found")
        void shouldCreateSuccessfully() {
            // Arrange
            var cmd = new CreateBillingAccountCommand(STUDENT_ID, DNI);
            var expected = newAccount();
            given(externalIamService.fetchCurrentAcademyId()).willReturn(Optional.of(ACADEMY_ID));
            given(billingAccountRepository.save(any(BillingAccount.class))).willReturn(expected);

            // Act
            var result = billingAccountService.handle(cmd);

            // Assert
            assertThat(result).isPresent();
            then(billingAccountRepository).should().save(any(BillingAccount.class));
            then(externalIamService).should().fetchCurrentAcademyId();
        }

        @Test
        @DisplayName("should throw error when academy is not found")
        void shouldThrowWhenNoAcademy() {
            // Arrange
            var cmd = new CreateBillingAccountCommand(STUDENT_ID, DNI);
            given(externalIamService.fetchCurrentAcademyId()).willReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> billingAccountService.handle(cmd))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("No academy found");
        }
    }

    // ── assignInvoice ─────────────────────────────────────────────────────

    @Nested
    @DisplayName("handle(AssignInvoiceToBillingAccountCommand)")
    class AssignInvoice {

        @Test
        @DisplayName("should assign invoice successfully")
        void shouldAssignSuccessfully() {
            // Arrange
            Long accountId = 1L;
            var account = newAccount();
            var cmd = new AssignInvoiceToBillingAccountCommand(
                    InvoiceType.STUDENT_MONTHLY_FEE, AMOUNT, "Test",
                    LocalDate.now(), LocalDate.now().plusDays(30),
                    InvoiceStatus.PENDING, accountId
            );

            given(billingAccountRepository.findById(accountId)).willReturn(Optional.of(account));
            given(billingAccountRepository.save(account)).willReturn(account);

            // Act
            var result = billingAccountService.handle(cmd);

            // Assert
            assertThat(result).isPresent();
            assertThat(account.getInvoices()).hasSize(1);
            then(billingAccountRepository).should().save(account);
        }
    }

    // ── deleteInvoice ─────────────────────────────────────────────────────

    @Nested
    @DisplayName("handle(DeleteInvoiceCommand)")
    class DeleteInvoice {

        @Test
        @DisplayName("should delete a pending invoice successfully")
        void shouldDeleteSuccessfully() {
            // Arrange
            Long accountId = 1L;
            Long invoiceId = 5L;
            var account = newAccount();
            // Asignar una factura manualmente para tener qué borrar
            account.assignInvoice(new AssignInvoiceToBillingAccountCommand(
                    InvoiceType.STUDENT_MONTHLY_FEE, AMOUNT, "Test",
                    LocalDate.now(), LocalDate.now().plusDays(30),
                    InvoiceStatus.PENDING, accountId
            ));

            // Inyectar ID a la factura vía Reflection (necesario porque el ID lo pone la BD)
            setInvoiceId(account.getInvoices().get(0), invoiceId);

            var cmd = new DeleteInvoiceCommand(accountId, invoiceId);
            given(billingAccountRepository.findById(accountId)).willReturn(Optional.of(account));

            // Act
            billingAccountService.handle(cmd);

            // Assert
            assertThat(account.getInvoices()).isEmpty();
            then(billingAccountRepository).should().save(account);
        }

        @Test
        @DisplayName("should throw exception when deleting paid invoice")
        void shouldThrowWhenPaid() {
            // Arrange
            Long accountId = 1L;
            Long invoiceId = 5L;
            var account = newAccount();
            account.assignInvoice(new AssignInvoiceToBillingAccountCommand(
                    InvoiceType.STUDENT_MONTHLY_FEE, AMOUNT, "Test",
                    LocalDate.now(), LocalDate.now().plusDays(30),
                    InvoiceStatus.PAID, accountId // Factura PAGADA
            ));
            setInvoiceId(account.getInvoices().get(0), invoiceId);

            var cmd = new DeleteInvoiceCommand(accountId, invoiceId);
            given(billingAccountRepository.findById(accountId)).willReturn(Optional.of(account));

            // Act & Assert
            assertThatThrownBy(() -> billingAccountService.handle(cmd))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("Cannot delete a paid invoice.");
        }
    }

// ── Helper Reflection ───────────────────────────────

    /**
     * Inyecta un ID manualmente en una entidad.
     * Busca el campo 'id' en la clase actual y en todas sus superclases.
     */
    private void setInvoiceId(Invoice invoice, Long id) {
        try {
            Class<?> clazz = invoice.getClass();
            java.lang.reflect.Field field = null;

            while (clazz != null && field == null) {
                try {
                    field = clazz.getDeclaredField("id");
                } catch (NoSuchFieldException e) {
                    clazz = clazz.getSuperclass();
                }
            }

            if (field == null) {
                throw new RuntimeException("No se encontró el campo 'id' en la jerarquía de " + invoice.getClass().getName());
            }

            field.setAccessible(true);
            field.set(invoice, id);
        } catch (Exception e) {
            throw new RuntimeException("Reflection failed: " + e.getMessage(), e);
        }
    }
}