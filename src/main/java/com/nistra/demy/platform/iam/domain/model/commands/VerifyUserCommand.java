package com.nistra.demy.platform.iam.domain.model.commands;

public record VerifyUserCommand(
        String email,
        String code
) {
    public VerifyUserCommand {
        if (email == null || email.isBlank())
            throw new IllegalArgumentException("Email cannot be null or empty");
        if (code == null || code.isBlank())
            throw new IllegalArgumentException("Code cannot be null or empty");
    }
}
