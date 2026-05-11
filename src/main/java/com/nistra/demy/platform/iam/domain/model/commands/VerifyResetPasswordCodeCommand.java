package com.nistra.demy.platform.iam.domain.model.commands;

import com.nistra.demy.platform.shared.domain.model.valueobjects.EmailAddress;

public record VerifyResetPasswordCodeCommand(
        EmailAddress emailAddress,
        String code
) {
    public VerifyResetPasswordCodeCommand {
        if (emailAddress == null)
            throw new IllegalArgumentException("Email address cannot be null");
        if (code == null || code.isBlank())
            throw new IllegalArgumentException("Verification code cannot be null or blank");
    }
}
