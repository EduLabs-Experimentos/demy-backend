package com.nistra.demy.platform.iam.interfaces.rest.resources;

public record ResendVerificationCodeResource(
        String email
) {
    public ResendVerificationCodeResource {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
    }
}
