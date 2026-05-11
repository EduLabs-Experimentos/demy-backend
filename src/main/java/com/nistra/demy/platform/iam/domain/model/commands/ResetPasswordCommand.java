package com.nistra.demy.platform.iam.domain.model.commands;

import com.nistra.demy.platform.shared.domain.model.valueobjects.EmailAddress;

public record ResetPasswordCommand(
    EmailAddress emailAddress,
    String password
) {
    public ResetPasswordCommand {
        if (emailAddress == null)
            throw new IllegalArgumentException("Email cannot be null");
        if (password == null || password.isBlank())
            throw new IllegalArgumentException("Password cannot be null or blank");
    }
}
