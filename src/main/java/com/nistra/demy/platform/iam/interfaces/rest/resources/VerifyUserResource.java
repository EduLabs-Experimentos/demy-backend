package com.nistra.demy.platform.iam.interfaces.rest.resources;

public record VerifyUserResource(
        String email,
        String code
) {
    public VerifyUserResource {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Code cannot be null or empty");
        }
    }
}
