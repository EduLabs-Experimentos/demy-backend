package com.nistra.demy.platform.attendance.domain.model.aggregates;

import com.nistra.demy.platform.attendance.domain.model.commands.CreateClassAttendanceCommand;
import com.nistra.demy.platform.attendance.domain.model.entities.AttendanceRecord;
import com.nistra.demy.platform.attendance.domain.model.valueobjects.AttendanceInput;
import com.nistra.demy.platform.attendance.domain.model.valueobjects.AttendanceStatus;
import com.nistra.demy.platform.attendance.domain.model.valueobjects.ClassSessionId;
import com.nistra.demy.platform.shared.domain.model.valueobjects.AcademyId;
import com.nistra.demy.platform.shared.domain.model.valueobjects.DniNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("ClassAttendance Aggregate")
class ClassAttendanceTest {

    // ── Shared fixtures ───────────────────────────────────────────────────

    private static final AcademyId      ACADEMY_ID  = new AcademyId(1L);
    private static final ClassSessionId SESSION_ID  = new ClassSessionId(10L);
    private static final DniNumber      DNI_ABSENT  = new DniNumber("87654321");
    private static final DniNumber      DNI_PRESENT = new DniNumber("12345678");
    private static final DniNumber      DNI_UNKNOWN = new DniNumber("00000001");

    /** Builds a valid command with today's date and two enrolled students. */
    private static CreateClassAttendanceCommand buildCommand() {
        return new CreateClassAttendanceCommand(
                SESSION_ID,
                LocalDate.now(),
                List.of(
                        new AttendanceInput(DNI_ABSENT,  AttendanceStatus.ABSENT),
                        new AttendanceInput(DNI_PRESENT, AttendanceStatus.PRESENT)
                )
        );
    }

    private ClassAttendance classAttendance;

    @BeforeEach
    void setUp() {
        // Arrange: fresh aggregate before every test
        classAttendance = new ClassAttendance(ACADEMY_ID, buildCommand());
    }

    // ── getRecordByDniOrThrow ─────────────────────────────────────────────

    @Nested
    @DisplayName("getRecordByDniOrThrow()")
    class GetRecordByDniOrThrow {

        @Test
        @DisplayName("should throw IllegalArgumentException when DNI is not found in the aggregate")
        void shouldThrowWhenDniNotFound() {
            // Act & Assert
            assertThatThrownBy(() -> classAttendance.getRecordByDniOrThrow(DNI_UNKNOWN))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("does not exist");
        }

        @Test
        @DisplayName("should return the AttendanceRecord when DNI exists")
        void shouldReturnRecordWhenDniExists() {
            // Act
            AttendanceRecord record = classAttendance.getRecordByDniOrThrow(DNI_ABSENT);

            // Assert
            assertThat(record).isNotNull();
            assertThat(record.getDni()).isEqualTo(DNI_ABSENT);
        }
    }

    // ── updateRecordStatus ────────────────────────────────────────────────

    @Nested
    @DisplayName("updateRecordStatus()")
    class UpdateRecordStatus {

        @Test
        @DisplayName("should update record status successfully from ABSENT to PRESENT")
        void shouldUpdateRecordStatusSuccessfully() {
            // Act
            classAttendance.updateRecordStatus(DNI_ABSENT, AttendanceStatus.PRESENT);

            // Assert
            AttendanceRecord updated = classAttendance.getRecordByDniOrThrow(DNI_ABSENT);
            assertThat(updated.getStatus()).isEqualTo(AttendanceStatus.PRESENT);
        }

        @Test
        @DisplayName("should update record status successfully from ABSENT to EXCUSED")
        void shouldUpdateRecordStatusToExcused() {
            // Act
            classAttendance.updateRecordStatus(DNI_ABSENT, AttendanceStatus.EXCUSED);

            // Assert
            AttendanceRecord updated = classAttendance.getRecordByDniOrThrow(DNI_ABSENT);
            assertThat(updated.getStatus()).isEqualTo(AttendanceStatus.EXCUSED);
        }

        @Test
        @DisplayName("should throw IllegalArgumentException when updating a non-enrolled DNI")
        void shouldThrowWhenUpdatingNonExistentDni() {
            // Act & Assert
            assertThatThrownBy(() -> classAttendance.updateRecordStatus(DNI_UNKNOWN, AttendanceStatus.PRESENT))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    // ── addAttendance ─────────────────────────────────────────────────────

    @Nested
    @DisplayName("addAttendance()")
    class AddAttendance {

        @Test
        @DisplayName("should add a new AttendanceRecord to the aggregate's list")
        void shouldAddAttendanceRecord() {
            // Arrange
            DniNumber newDni = new DniNumber("11111111");
            AttendanceRecord newRecord = new AttendanceRecord(classAttendance, newDni, AttendanceStatus.ABSENT);

            // Act
            classAttendance.addAttendance(newRecord);

            // Assert
            assertThat(classAttendance.getAttendance()).hasSize(3);
            assertThat(classAttendance.getRecordByDniOrThrow(newDni).getDni()).isEqualTo(newDni);
        }
    }

    // ── CreateClassAttendanceCommand business rules ───────────────────────

    @Nested
    @DisplayName("CreateClassAttendanceCommand validation")
    class CommandValidation {

        @Test
        @DisplayName("should reject a past date when building the command")
        void shouldRejectPastDate() {
            // Arrange
            LocalDate yesterday = LocalDate.now().minusDays(1);

            // Act & Assert
            assertThatThrownBy(() -> new CreateClassAttendanceCommand(
                    SESSION_ID,
                    yesterday,
                    List.of(new AttendanceInput(DNI_ABSENT, AttendanceStatus.ABSENT))
            )).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("should reject a null classSessionId when building the command")
        void shouldRejectNullSessionId() {
            // Act & Assert
            assertThatThrownBy(() -> new CreateClassAttendanceCommand(
                    null,
                    LocalDate.now(),
                    List.of(new AttendanceInput(DNI_ABSENT, AttendanceStatus.ABSENT))
            )).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("should reject an empty attendance list when building the command")
        void shouldRejectEmptyAttendanceList() {
            // Act & Assert
            assertThatThrownBy(() -> new CreateClassAttendanceCommand(
                    SESSION_ID,
                    LocalDate.now(),
                    List.of()
            )).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("should accept today's date and create the aggregate successfully")
        void shouldAcceptTodayDate() {
            // Act
            ClassAttendance attendance = new ClassAttendance(ACADEMY_ID, buildCommand());

            // Assert
            assertThat(attendance.getDate()).isEqualTo(LocalDate.now());
            assertThat(attendance.getAcademyId()).isEqualTo(ACADEMY_ID);
            assertThat(attendance.getAttendance()).hasSize(2);
        }
    }
}