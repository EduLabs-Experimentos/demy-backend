package com.nistra.demy.platform.attendance.domain.model.valueobjects;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("ClassSessionId Value Object")
class ClassSessionIdTest {

    // -------------------------------------------------------------------------
    // shouldRejectNonPositiveId (Parameterized)
    // -------------------------------------------------------------------------

    @ParameterizedTest(name = "[{index}] id={0} → should throw IllegalArgumentException")
    @ValueSource(longs = {0L, -1L, -100L})
    @DisplayName("should reject zero or negative IDs")
    void shouldRejectNonPositiveId(long invalidId) {
        // Act & Assert
        assertThatThrownBy(() -> new ClassSessionId(invalidId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("greater than zero");
    }

    // -------------------------------------------------------------------------
    // Valid construction
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("should create ClassSessionId successfully with a positive value")
    void shouldCreateSuccessfullyWithPositiveId() {
        // Arrange
        long validId = 42L;

        // Act
        ClassSessionId sessionId = new ClassSessionId(validId);

        // Assert
        assertThat(sessionId.value()).isEqualTo(validId);
    }

    @Test
    @DisplayName("should create ClassSessionId with the minimum valid value (1)")
    void shouldAcceptMinimumValidId() {
        // Arrange & Act
        ClassSessionId sessionId = new ClassSessionId(1L);

        // Assert
        assertThat(sessionId.value()).isEqualTo(1L);
    }

    // -------------------------------------------------------------------------
    // Equality (Value Object contract)
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("should be equal when the wrapped ID value is the same")
    void shouldBeEqualWithSameValue() {
        // Arrange
        ClassSessionId a = new ClassSessionId(10L);
        ClassSessionId b = new ClassSessionId(10L);

        // Act & Assert
        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    @DisplayName("should not be equal when the wrapped ID values differ")
    void shouldNotBeEqualWithDifferentValues() {
        // Arrange
        ClassSessionId a = new ClassSessionId(1L);
        ClassSessionId b = new ClassSessionId(2L);

        // Act & Assert
        assertThat(a).isNotEqualTo(b);
    }
}