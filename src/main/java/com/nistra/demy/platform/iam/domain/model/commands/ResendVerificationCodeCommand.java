package com.nistra.demy.platform.iam.domain.model.commands;

public record ResendVerificationCodeCommand(
        String email
) {
    public ResendVerificationCodeCommand {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
    }
}
