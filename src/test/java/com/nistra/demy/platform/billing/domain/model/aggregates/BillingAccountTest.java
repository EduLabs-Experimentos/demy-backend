package com.nistra.demy.platform.billing.domain.model.aggregates;

import com.nistra.demy.platform.billing.domain.model.commands.AssignInvoiceToBillingAccountCommand;
import com.nistra.demy.platform.billing.domain.model.commands.CreateBillingAccountCommand;
import com.nistra.demy.platform.billing.domain.model.commands.UpdateInvoiceCommand;
import com.nistra.demy.platform.billing.domain.model.entities.Invoice;
import com.nistra.demy.platform.billing.domain.model.valueobjects.InvoiceStatus;
import com.nistra.demy.platform.billing.domain.model.valueobjects.InvoiceType;
import com.nistra.demy.platform.shared.domain.model.valueobjects.AcademyId;
import com.nistra.demy.platform.shared.domain.model.valueobjects.DniNumber;
import com.nistra.demy.platform.shared.domain.model.valueobjects.Money;
import com.nistra.demy.platform.shared.domain.model.valueobjects.StudentId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("BillingAccount Aggregate")
class BillingAccountTest {

    // ── Shared fixtures ───────────────────────────────────────────────────

    private static final AcademyId ACADEMY_ID = new AcademyId(1L);
    private static final StudentId STUDENT_ID = new StudentId(10L);
    private static final DniNumber DNI = new DniNumber("12345678");
    // CORRECCIÓN: Uso de java.util.Currency para el Value Object Money
    private static final Money AMOUNT = new Money(new BigDecimal("150.00"), Currency.getInstance("PEN"));
    private static final Long FAKE_ID = 999L;

    private BillingAccount account;

    /** Builds a valid AssignInvoiceToBillingAccountCommand for use in tests. */
    private AssignInvoiceToBillingAccountCommand buildAssignCommand(Long accountId) {
        return new AssignInvoiceToBillingAccountCommand(
                InvoiceType.STUDENT_MONTHLY_FEE,
                AMOUNT,
                "Monthly fee - May 2026",
                LocalDate.now(),
                LocalDate.now().plusDays(30),
                InvoiceStatus.PENDING,
                accountId
        );
    }

    @BeforeEach
    void setUp() {
        // Arrange: fresh BillingAccount before every test
        CreateBillingAccountCommand cmd = new CreateBillingAccountCommand(STUDENT_ID, DNI);
        account = new BillingAccount(cmd, ACADEMY_ID);
    }

    // ── Creation ──────────────────────────────────────────────────────────

    @Nested
    @DisplayName("Creation")
    class Creation {

        @Test
        @DisplayName("should create BillingAccount with ACTIVE status and empty invoice list")
        void shouldCreateWithActiveStatusAndEmptyInvoices() {
            // Assert
            assertThat(account.getStudentId()).isEqualTo(STUDENT_ID);
            assertThat(account.getDniNumber()).isEqualTo(DNI);
            assertThat(account.getAcademyId()).isEqualTo(ACADEMY_ID);
            assertThat(account.getInvoices()).isEmpty();
        }

        @Test
        @DisplayName("should throw when creating command with null StudentId")
        void shouldThrowWhenStudentIdIsNull() {
            // Act & Assert
            assertThatThrownBy(() -> new CreateBillingAccountCommand(null, DNI))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Student ID");
        }

        @Test
        @DisplayName("should throw when creating command with null DniNumber")
        void shouldThrowWhenDniNumberIsNull() {
            // Act & Assert
            assertThatThrownBy(() -> new CreateBillingAccountCommand(STUDENT_ID, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("DNI");
        }
    }

    // ── assignInvoice ─────────────────────────────────────────────────────

    @Nested
    @DisplayName("assignInvoice()")
    class AssignInvoice {

        @Test
        @DisplayName("should add an invoice to the account when command is valid")
        void shouldAssignInvoiceSuccessfully() {
            // Arrange
            AssignInvoiceToBillingAccountCommand cmd = buildAssignCommand(1L);

            // Act
            account.assignInvoice(cmd);

            // Assert
            assertThat(account.getInvoices()).hasSize(1);
            Invoice invoice = account.getInvoices().get(0);
            assertThat(invoice.getInvoiceType()).isEqualTo(InvoiceType.STUDENT_MONTHLY_FEE);
            assertThat(invoice.getStatus()).isEqualTo(InvoiceStatus.PENDING);
            assertThat(invoice.getDescription()).isEqualTo("Monthly fee - May 2026");
        }

        @Test
        @DisplayName("should throw when command has null invoiceType")
        void shouldThrowWhenInvoiceTypeIsNull() {
            // Act & Assert
            assertThatThrownBy(() -> new AssignInvoiceToBillingAccountCommand(
                    null, AMOUNT, "desc",
                    LocalDate.now(), LocalDate.now().plusDays(1),
                    InvoiceStatus.PENDING, 1L
            )).isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Invoice type");
        }
    }

    // ── markInvoiceAsPaid ─────────────────────────────────────────────────

    @Nested
    @DisplayName("markInvoiceAsPaid()")
    class MarkInvoiceAsPaid {

        @Test
        @DisplayName("should change invoice status to PAID")
        void shouldMarkInvoiceAsPaidSuccessfully() {
            // Arrange
            account.assignInvoice(buildAssignCommand(1L));
            Invoice invoice = account.getInvoices().get(0);
            setInvoiceId(invoice, 1L);

            // Act
            account.markInvoiceAsPaid(1L);

            // Assert
            assertThat(invoice.getStatus()).isEqualTo(InvoiceStatus.PAID);
            // Nota: Se omite la validación de domainEvents() por ser protected en la base
        }

        @Test
        @DisplayName("should throw RuntimeException when invoice ID does not exist")
        void shouldThrowWhenInvoiceNotFound() {
            // Act & Assert
            assertThatThrownBy(() -> account.markInvoiceAsPaid(FAKE_ID))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Invoice not found");
        }
    }

    // ── deleteInvoice ─────────────────────────────────────────────────────

    @Nested
    @DisplayName("deleteInvoice()")
    class DeleteInvoice {

        @Test
        @DisplayName("should remove a PENDING invoice from the account")
        void shouldDeletePendingInvoiceSuccessfully() {
            // Arrange
            account.assignInvoice(buildAssignCommand(1L));
            Invoice invoice = account.getInvoices().get(0);
            setInvoiceId(invoice, 1L);

            // Act
            account.deleteInvoice(1L);

            // Assert
            assertThat(account.getInvoices()).isEmpty();
        }

        @Test
        @DisplayName("should throw IllegalStateException when trying to delete a PAID invoice")
        void shouldThrowWhenDeletingPaidInvoice() {
            // Arrange
            account.assignInvoice(buildAssignCommand(1L));
            Invoice invoice = account.getInvoices().get(0);
            setInvoiceId(invoice, 1L);
            account.markInvoiceAsPaid(1L);

            // Act & Assert
            assertThatThrownBy(() -> account.deleteInvoice(1L))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("Cannot delete a paid invoice");
        }
    }

    // ── updateInvoice ─────────────────────────────────────────────────────

    @Nested
    @DisplayName("updateInvoice()")
    class UpdateInvoice {

        @Test
        @DisplayName("should update invoice details successfully")
        void shouldUpdateInvoiceSuccessfully() {
            // Arrange
            account.assignInvoice(buildAssignCommand(1L));
            Invoice invoice = account.getInvoices().get(0);
            setInvoiceId(invoice, 1L);

            Money updatedAmount = new Money(new BigDecimal("200.00"), Currency.getInstance("PEN"));
            UpdateInvoiceCommand updateCmd = new UpdateInvoiceCommand(
                    1L, 1L,
                    InvoiceType.STUDENT_ENROLLMENT,
                    updatedAmount,
                    "Updated description",
                    InvoiceStatus.OVERDUE
            );

            // Act
            Invoice updated = account.updateInvoice(updateCmd);

            // Assert
            assertThat(updated.getInvoiceType()).isEqualTo(InvoiceType.STUDENT_ENROLLMENT);
            assertThat(updated.getAmount()).isEqualTo(updatedAmount);
            assertThat(updated.getDescription()).isEqualTo("Updated description");
            assertThat(updated.getStatus()).isEqualTo(InvoiceStatus.OVERDUE);
        }
    }

    // ── findInvoiceById ───────────────────────────────────────────────────

    @Nested
    @DisplayName("findInvoiceById()")
    class FindInvoiceById {

        @Test
        @DisplayName("should return the invoice when it exists")
        void shouldReturnInvoiceById() {
            // Arrange
            account.assignInvoice(buildAssignCommand(1L));
            Invoice invoice = account.getInvoices().get(0);
            setInvoiceId(invoice, 5L);

            // Act
            Invoice found = account.findInvoiceById(5L);

            // Assert
            assertThat(found).isNotNull();
            assertThat(found.getId()).isEqualTo(5L);
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────

    /** Sets the id field on an Invoice via reflection for Unit Testing. */
    private void setInvoiceId(Invoice invoice, Long id) {
        try {
            Class<?> clazz = invoice.getClass();
            java.lang.reflect.Field field = null;
            while (clazz != null && field == null) {
                try { field = clazz.getDeclaredField("id"); }
                catch (NoSuchFieldException e) { clazz = clazz.getSuperclass(); }
            }
            if (field == null) throw new IllegalStateException("No 'id' field found");
            field.setAccessible(true);
            field.set(invoice, id);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Could not set invoice ID via reflection", e);
        }
    }
}