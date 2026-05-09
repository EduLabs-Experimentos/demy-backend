package com.nistra.demy.platform.attendance.infrastructure.persistence.jpa.repositories;

import com.nistra.demy.platform.attendance.domain.model.aggregates.ClassAttendance;
import com.nistra.demy.platform.attendance.domain.model.commands.CreateClassAttendanceCommand;
import com.nistra.demy.platform.attendance.domain.model.entities.AttendanceRecord;
import com.nistra.demy.platform.attendance.domain.model.valueobjects.AttendanceInput;
import com.nistra.demy.platform.attendance.domain.model.valueobjects.AttendanceStatus;
import com.nistra.demy.platform.attendance.domain.model.valueobjects.ClassSessionId;
import com.nistra.demy.platform.shared.domain.model.valueobjects.AcademyId;
import com.nistra.demy.platform.shared.domain.model.valueobjects.DniNumber;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link ClassAttendanceRepository}.
 *
 * <p>Uses an H2 in-memory database (auto-configured by {@code @DataJpaTest}).
 * {@link TestEntityManager#flush()} + {@link TestEntityManager#clear()} force
 * a real DB round-trip so every assertion reflects persisted state, not the
 * first-level cache.</p>
 */
@DataJpaTest
@ActiveProfiles("test")
@DisplayName("ClassAttendanceRepository Integration Tests")
class ClassAttendanceRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ClassAttendanceRepository repository;

    // ── Shared fixtures ───────────────────────────────────────────────────

    private static final AcademyId      ACADEMY_ID = new AcademyId(1L);
    private static final ClassSessionId SESSION_1  = new ClassSessionId(1L);
    private static final ClassSessionId SESSION_2  = new ClassSessionId(2L);
    private static final ClassSessionId SESSION_3  = new ClassSessionId(3L);
    private static final ClassSessionId SESSION_4  = new ClassSessionId(10L);

    private static final DniNumber DNI_1 = new DniNumber("11111111");
    private static final DniNumber DNI_2 = new DniNumber("22222222");
    private static final DniNumber DNI_3 = new DniNumber("33333333");
    private static final DniNumber DNI_4 = new DniNumber("44444444");
    private static final DniNumber DNI_5 = new DniNumber("55555555");

    /** Convenience builder wired to {@code ACADEMY_ID} and today's date. */
    private ClassAttendance buildAggregate(ClassSessionId sessionId,
                                           List<AttendanceInput> inputs) {
        CreateClassAttendanceCommand cmd =
                new CreateClassAttendanceCommand(sessionId, LocalDate.now(), inputs);
        return new ClassAttendance(ACADEMY_ID, cmd);
    }

    // ── shouldPersistAndLoadWithEmbeddedValueObjects ──────────────────────

    @Test
    @DisplayName("should persist and load ClassAttendance with embedded Value Objects")
    void shouldPersistAndLoadWithEmbeddedValueObjects() {
        // Arrange
        ClassAttendance attendance = buildAggregate(
                SESSION_1,
                List.of(new AttendanceInput(DNI_1, AttendanceStatus.PRESENT))
        );

        // Act
        ClassAttendance saved = repository.save(attendance);
        entityManager.flush();
        entityManager.clear(); // evict first-level cache → next find hits DB

        Optional<ClassAttendance> found = repository.findById(saved.getId());

        // Assert
        assertThat(found).isPresent();
        ClassAttendance loaded = found.get();
        assertThat(loaded.getClassSessionId()).isEqualTo(SESSION_1);
        assertThat(loaded.getDate()).isEqualTo(LocalDate.now());
        assertThat(loaded.getAcademyId()).isEqualTo(ACADEMY_ID);
    }

    // ── shouldCascadeAttendanceRecordsOnSave ──────────────────────────────

    @Test
    @DisplayName("should cascade and persist AttendanceRecords when saving the Aggregate")
    void shouldCascadeAttendanceRecordsOnSave() {
        // Arrange
        ClassAttendance attendance = buildAggregate(
                SESSION_2,
                List.of(
                        new AttendanceInput(DNI_1, AttendanceStatus.PRESENT),
                        new AttendanceInput(DNI_2, AttendanceStatus.ABSENT),
                        new AttendanceInput(DNI_3, AttendanceStatus.EXCUSED)
                )
        );

        // Act
        ClassAttendance saved = repository.save(attendance);
        entityManager.flush();
        entityManager.clear();

        Optional<ClassAttendance> found = repository.findByIdAndAcademyId(saved.getId(), ACADEMY_ID);

        // Assert
        assertThat(found).isPresent();
        List<AttendanceRecord> records = found.get().getAttendance();
        assertThat(records).hasSize(3);
        assertThat(records)
                .extracting(r -> r.getDni().dniNumber())
                .containsExactlyInAnyOrder("11111111", "22222222", "33333333");
    }

    // ── shouldRemoveOrphanedRecords ───────────────────────────────────────

    @Test
    @DisplayName("should remove orphaned AttendanceRecords due to orphanRemoval = true")
    void shouldRemoveOrphanedRecords() {
        // Arrange — persist aggregate with two records
        ClassAttendance attendance = buildAggregate(
                SESSION_3,
                List.of(
                        new AttendanceInput(DNI_4, AttendanceStatus.PRESENT),
                        new AttendanceInput(DNI_5, AttendanceStatus.ABSENT)
                )
        );
        ClassAttendance saved = repository.save(attendance);
        entityManager.flush();
        entityManager.clear();

        // Act — reload, remove DNI_4's record, save
        ClassAttendance loaded = repository.findById(saved.getId()).orElseThrow();
        loaded.getAttendance()
                .removeIf(r -> r.getDni().equals(DNI_4));
        repository.save(loaded);
        entityManager.flush();
        entityManager.clear();

        // Assert — only DNI_5 survives
        ClassAttendance reloaded = repository.findById(saved.getId()).orElseThrow();
        assertThat(reloaded.getAttendance()).hasSize(1);
        assertThat(reloaded.getAttendance().get(0).getDni()).isEqualTo(DNI_5);
    }

    // ── existsByAcademyIdAndClassSessionIdAndDate ─────────────────────────

    @Test
    @DisplayName("should return true when attendance for academy, session, and date already exists")
    void shouldDetectDuplicateAttendance() {
        // Arrange
        ClassAttendance attendance = buildAggregate(
                SESSION_4,
                List.of(new AttendanceInput(DNI_1, AttendanceStatus.PRESENT))
        );
        repository.save(attendance);
        entityManager.flush();
        entityManager.clear();

        // Act
        boolean exists = repository.existsByAcademyIdAndClassSessionIdAndDate(
                ACADEMY_ID, SESSION_4, LocalDate.now());

        // Assert
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("should return false when no attendance exists for the given academy, session, and date")
    void shouldReturnFalseWhenNoAttendanceExists() {
        // Act — nothing persisted for SESSION_4 yet in this test
        boolean exists = repository.existsByAcademyIdAndClassSessionIdAndDate(
                ACADEMY_ID, SESSION_4, LocalDate.now());

        // Assert
        assertThat(exists).isFalse();
    }

    // ── findAllByAcademyId ────────────────────────────────────────────────

    @Test
    @DisplayName("should return all ClassAttendance records belonging to the given AcademyId")
    void shouldFindAllByAcademyId() {
        // Arrange — two sessions for ACADEMY_ID, one for a different academy
        repository.save(buildAggregate(SESSION_1,
                List.of(new AttendanceInput(DNI_1, AttendanceStatus.PRESENT))));
        repository.save(buildAggregate(SESSION_2,
                List.of(new AttendanceInput(DNI_2, AttendanceStatus.ABSENT))));

        AcademyId otherAcademy = new AcademyId(99L);
        ClassAttendance other = new ClassAttendance(otherAcademy,
                new CreateClassAttendanceCommand(SESSION_3, LocalDate.now(),
                        List.of(new AttendanceInput(DNI_3, AttendanceStatus.EXCUSED))));
        repository.save(other);
        entityManager.flush();
        entityManager.clear();

        // Act
        List<ClassAttendance> results = repository.findAllByAcademyId(ACADEMY_ID);

        // Assert
        assertThat(results).hasSize(2);
        assertThat(results)
                .allMatch(a -> a.getAcademyId().equals(ACADEMY_ID));
    }
}