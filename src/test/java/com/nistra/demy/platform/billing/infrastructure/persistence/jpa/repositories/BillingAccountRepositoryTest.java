package com.nistra.demy.platform.billing.infrastructure.persistence.jpa.repositories;

import com.nistra.demy.platform.billing.domain.model.aggregates.BillingAccount;
import com.nistra.demy.platform.billing.domain.model.commands.AssignInvoiceToBillingAccountCommand;
import com.nistra.demy.platform.billing.domain.model.commands.CreateBillingAccountCommand;
import com.nistra.demy.platform.billing.domain.model.entities.Invoice;
import com.nistra.demy.platform.billing.domain.model.valueobjects.InvoiceStatus;
import com.nistra.demy.platform.billing.domain.model.valueobjects.InvoiceType;
import com.nistra.demy.platform.billing.infrastructure.persistence.jpa.repositories.BillingAccountRepository;
import com.nistra.demy.platform.shared.domain.model.valueobjects.AcademyId;
import com.nistra.demy.platform.shared.domain.model.valueobjects.DniNumber;
import com.nistra.demy.platform.shared.domain.model.valueobjects.Money;
import com.nistra.demy.platform.shared.domain.model.valueobjects.StudentId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("BillingAccountRepository Integration Tests")
class BillingAccountRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BillingAccountRepository repository;

    // ── Shared fixtures ───────────────────────────────────────────────────

    private static final AcademyId ACADEMY_1   = new AcademyId(1L);
    private static final AcademyId ACADEMY_2   = new AcademyId(2L);
    private static final StudentId STUDENT_A   = new StudentId(10L);
    private static final StudentId STUDENT_B   = new StudentId(20L);
    private static final DniNumber DNI_A       = new DniNumber("12345678");
    private static final DniNumber DNI_B       = new DniNumber("87654321");
    private static final Money AMOUNT = new Money(new BigDecimal("200.00"), java.util.Currency.getInstance("PEN"));

    private BillingAccount buildAccount(StudentId studentId, DniNumber dni, AcademyId academyId) {
        return new BillingAccount(
                new CreateBillingAccountCommand(studentId, dni),
                academyId
        );
    }

    private AssignInvoiceToBillingAccountCommand buildAssignCmd(Long accountId) {
        return new AssignInvoiceToBillingAccountCommand(
                InvoiceType.STUDENT_ENROLLMENT,
                AMOUNT,
                "Enrollment fee",
                LocalDate.now(),
                LocalDate.now().plusDays(30),
                InvoiceStatus.PENDING,
                accountId
        );
    }

    // ── shouldPersistAndLoadWithEmbeddedValueObjects ──────────────────────

    @Test
    @DisplayName("should persist and reload BillingAccount with embedded Value Objects")
    void shouldPersistAndLoadWithEmbeddedValueObjects() {
        // Arrange
        BillingAccount account = buildAccount(STUDENT_A, DNI_A, ACADEMY_1);

        // Act
        BillingAccount saved = repository.save(account);
        entityManager.flush();
        entityManager.clear();

        Optional<BillingAccount> found = repository.findById(saved.getId());

        // Assert
        assertThat(found).isPresent();
        BillingAccount loaded = found.get();
        assertThat(loaded.getStudentId()).isEqualTo(STUDENT_A);
        assertThat(loaded.getDniNumber()).isEqualTo(DNI_A);
        assertThat(loaded.getAcademyId()).isEqualTo(ACADEMY_1);
    }

    // ── shouldCascadeInvoicesOnSave ───────────────────────────────────────

    @Test
    @DisplayName("should cascade and persist Invoices when saving the BillingAccount")
    void shouldCascadeInvoicesOnSave() {
        // Arrange
        BillingAccount account = buildAccount(STUDENT_A, DNI_A, ACADEMY_1);
        BillingAccount saved = repository.save(account);
        saved.assignInvoice(buildAssignCmd(saved.getId()));
        saved.assignInvoice(buildAssignCmd(saved.getId()));
        repository.save(saved);
        entityManager.flush();
        entityManager.clear();

        // Act
        Optional<BillingAccount> found = repository.findById(saved.getId());

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getInvoices()).hasSize(2);
    }

    // ── shouldRemoveOrphanedInvoice ───────────────────────────────────────

    @Test
    @DisplayName("should delete orphaned Invoice when removed from the aggregate (orphanRemoval=true)")
    void shouldRemoveOrphanedInvoice() {
        // Arrange
        BillingAccount account = buildAccount(STUDENT_A, DNI_A, ACADEMY_1);
        BillingAccount saved = repository.save(account);
        saved.assignInvoice(buildAssignCmd(saved.getId()));
        saved.assignInvoice(buildAssignCmd(saved.getId()));
        repository.save(saved);
        entityManager.flush();
        entityManager.clear();

        // Act — reload and delete one invoice
        BillingAccount loaded = repository.findById(saved.getId()).orElseThrow();
        Long invoiceIdToRemove = loaded.getInvoices().get(0).getId();
        loaded.deleteInvoice(invoiceIdToRemove);
        repository.save(loaded);
        entityManager.flush();
        entityManager.clear();

        // Assert — only 1 invoice remains
        BillingAccount reloaded = repository.findById(saved.getId()).orElseThrow();
        assertThat(reloaded.getInvoices()).hasSize(1);
        assertThat(reloaded.getInvoices().get(0).getId()).isNotEqualTo(invoiceIdToRemove);
    }

    // ── findByStudentId ───────────────────────────────────────────────────

    @Test
    @DisplayName("should find BillingAccount by StudentId")
    void shouldFindByStudentId() {
        // Arrange
        repository.save(buildAccount(STUDENT_A, DNI_A, ACADEMY_1));
        entityManager.flush();
        entityManager.clear();

        // Act
        Optional<BillingAccount> result = repository.findByStudentId(STUDENT_A);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getStudentId()).isEqualTo(STUDENT_A);
    }

    @Test
    @DisplayName("should return empty when no BillingAccount exists for the StudentId")
    void shouldReturnEmptyWhenStudentNotFound() {
        // Act
        Optional<BillingAccount> result = repository.findByStudentId(new StudentId(999L));

        // Assert
        assertThat(result).isEmpty();
    }

    // ── findAllByAcademyId ────────────────────────────────────────────────

    @Test
    @DisplayName("should return all BillingAccounts belonging to the given AcademyId")
    void shouldFindAllByAcademyId() {
        // Arrange — two accounts for ACADEMY_1, one for ACADEMY_2
        repository.save(buildAccount(STUDENT_A, DNI_A, ACADEMY_1));
        repository.save(buildAccount(STUDENT_B, DNI_B, ACADEMY_1));
        repository.save(buildAccount(new StudentId(30L), new DniNumber("11111111"), ACADEMY_2));
        entityManager.flush();
        entityManager.clear();

        // Act
        List<BillingAccount> results = repository.findAllByAcademyId(ACADEMY_1);

        // Assert
        assertThat(results).hasSize(2);
        assertThat(results).allMatch(a -> a.getAcademyId().equals(ACADEMY_1));
    }
}