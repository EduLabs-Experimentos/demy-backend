package com.nistra.demy.platform.iam.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

import java.time.LocalDateTime;

@Embeddable
public record VerificationCode(
        String code,
        LocalDateTime expiration
) {
    public VerificationCode {
        if (code != null && code.isBlank())
            throw new IllegalArgumentException("Verification code cannot be empty");
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiration);
    }

    public boolean matches(String inputCode) {
        return code != null && code.equals(inputCode) && !isExpired();
    }
}
