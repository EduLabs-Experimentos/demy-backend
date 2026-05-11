package com.nistra.demy.platform.attendance.domain.model.valueobjects;

import com.nistra.demy.platform.shared.domain.model.valueobjects.DniNumber;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("AttendanceInput Value Object")
class AttendanceInputTest {

    private static final DniNumber VALID_DNI = new DniNumber("12345678");

    // ── shouldRejectNullDniOrStatus (Parameterized) ───────────────────────

    static Stream<Arguments> nullInputCombinations() {
        return Stream.of(
                Arguments.of(null,      AttendanceStatus.PRESENT, "null DniNumber"),
                Arguments.of(null,      null,                     "null DniNumber and null status"),
                Arguments.of(VALID_DNI, null,                     "null status")
        );
    }

    @ParameterizedTest(name = "[{index}] {2} → should throw IllegalArgumentException")
    @MethodSource("nullInputCombinations")
    @DisplayName("should reject null DniNumber or null status")
    void shouldRejectNullDniOrStatus(DniNumber dni, AttendanceStatus status, String description) {
        // Act & Assert
        assertThatThrownBy(() -> new AttendanceInput(dni, status))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // ── DniNumber own validation (delegates to DniNumber record) ─────────

    @Test
    @DisplayName("should throw when constructing DniNumber with a non-8-digit value")
    void shouldRejectInvalidDniFormat() {
        // Act & Assert — DniNumber itself validates; AttendanceInput receives a valid VO
        assertThatThrownBy(() -> new DniNumber("123"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("8 characters");
    }

    @Test
    @DisplayName("should throw when constructing DniNumber with non-digit characters")
    void shouldRejectNonDigitDni() {
        // Act & Assert
        assertThatThrownBy(() -> new DniNumber("1234567A"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("digits");
    }

    // ── Valid construction ────────────────────────────────────────────────

    @Test
    @DisplayName("should create AttendanceInput successfully with valid DniNumber and status")
    void shouldCreateSuccessfullyWithValidArguments() {
        // Act
        AttendanceInput input = new AttendanceInput(VALID_DNI, AttendanceStatus.ABSENT);

        // Assert
        assertThat(input.dni()).isEqualTo(VALID_DNI);
        assertThat(input.status()).isEqualTo(AttendanceStatus.ABSENT);
    }

    // ── Equality (record contract) ────────────────────────────────────────

    @Test
    @DisplayName("should be equal when DniNumber and status are the same")
    void shouldBeEqualWithSameValues() {
        // Arrange
        AttendanceInput a = new AttendanceInput(VALID_DNI, AttendanceStatus.EXCUSED);
        AttendanceInput b = new AttendanceInput(VALID_DNI, AttendanceStatus.EXCUSED);

        // Act & Assert
        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    @DisplayName("should not be equal when status differs")
    void shouldNotBeEqualWhenStatusDiffers() {
        // Arrange
        AttendanceInput a = new AttendanceInput(VALID_DNI, AttendanceStatus.PRESENT);
        AttendanceInput b = new AttendanceInput(VALID_DNI, AttendanceStatus.ABSENT);

        // Act & Assert
        assertThat(a).isNotEqualTo(b);
    }
}