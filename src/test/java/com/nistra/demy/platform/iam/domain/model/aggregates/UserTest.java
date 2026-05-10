package com.nistra.demy.platform.iam.domain.model.aggregates;

import com.nistra.demy.platform.iam.domain.model.entities.Role;
import com.nistra.demy.platform.iam.domain.model.valueobjects.AccountStatus;
import com.nistra.demy.platform.iam.domain.model.valueobjects.TenantId;
import com.nistra.demy.platform.iam.domain.model.valueobjects.VerificationCode;
import com.nistra.demy.platform.iam.domain.model.valueobjects.VerificationStatus;
import com.nistra.demy.platform.shared.domain.model.valueobjects.EmailAddress;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the User aggregate root using the Arrange-Act-Assert (AAA) pattern.
 * Verifies core business rules: verification flow, activation guard,
 * multi-tenant association/disassociation, and password reset cycle.
 *
 * <p>Mockito is used to isolate User's guard logic from its value object
 * collaborators (VerificationCode, TenantId), enabling precise testing of
 * state transitions and invariants without depending on real time or
 * external identity matching.</p>
 *
 * @since 1.0.0
 */
class UserTest {

    // ==================== Verification Flow ====================

    @Test
    @DisplayName("Should activate user when verified with valid code")
    void shouldActivateUserWhenVerifiedWithValidCode() {
        // Arrange
        EmailAddress email = new EmailAddress("user@example.com");
        VerificationCode code = new VerificationCode("123456", LocalDateTime.now().plusMinutes(10));
        User user = new User(email, "passwordHash", code);

        // Act
        user.verifyUser("123456");

        // Assert
        assertEquals(VerificationStatus.VERIFIED, user.getVerificationStatus());
        assertEquals(AccountStatus.ACTIVE, user.getAccountStatus());
        assertNull(user.getVerificationCode().code(), "Verification code should be cleared after successful verification");
    }

    @Test
    @DisplayName("Should reject verification when code is invalid or expired")
    void shouldRejectVerificationWhenCodeIsInvalidOrExpired() {
        // Arrange
        EmailAddress email = new EmailAddress("user@example.com");
        VerificationCode mockCode = mock(VerificationCode.class);
        when(mockCode.matches("badCode")).thenReturn(false);
        User user = new User(email, "passwordHash", mockCode);

        // Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> user.verifyUser("badCode"));

        // Assert
        verify(mockCode).matches("badCode");
        assertEquals(VerificationStatus.NOT_VERIFIED, user.getVerificationStatus(),
                "User must remain NOT_VERIFIED after failed verification");
    }



    // ==================== Multi-Tenancy Guards  ====================

    @Test
    @DisplayName("Should reject tenant assignment when user already has a tenant")
    void shouldRejectTenantAssignmentWhenAlreadyAssigned() {
        // Arrange
        EmailAddress email = new EmailAddress("tenant@example.com");
        User user = new User(email, "passwordHash",
                new VerificationCode(null, null));

        TenantId mockTenant = mock(TenantId.class);
        when(mockTenant.isAssigned()).thenReturn(true);
        user.setTenantId(mockTenant);

        // Act
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> user.associateTenant(new TenantId(2L)));

        // Assert
        assertEquals("User is already associated with a tenant", exception.getMessage());
        verify(mockTenant, atLeastOnce()).isAssigned();
    }

    // ==================== Password Reset ====================

    @Test
    @DisplayName("Should reject password reset code when invalid")
    void shouldRejectPasswordResetCodeWhenInvalid() {
        // Arrange
        EmailAddress email = new EmailAddress("user@example.com");
        VerificationCode mockCode = mock(VerificationCode.class);
        when(mockCode.matches("wrongResetCode")).thenReturn(false);
        User user = new User(email, "oldPasswordHash", mockCode);

        // Act
        assertThrows(IllegalArgumentException.class,
                () -> user.verifyPasswordResetCode("wrongResetCode"));

        // Assert
        verify(mockCode).matches("wrongResetCode");
    }

    @Test
    @DisplayName("Should complete full password reset cycle")
    void shouldCompleteFullPasswordResetCycle() {
        // Arrange
        EmailAddress email = new EmailAddress("user@example.com");
        User user = new User(email, "oldPasswordHash",
                new VerificationCode(null, null));

        // Act
        user.assignNewPasswordVerificationCode("user@example.com", "resetCode", 10);
        user.verifyPasswordResetCode("resetCode");
        user.resetPassword("newPasswordHash");

        // Assert
        assertEquals("newPasswordHash", user.getPassword());
        assertNull(user.getVerificationCode().code(),
                "Verification code should be cleared after successful reset");
    }
}
