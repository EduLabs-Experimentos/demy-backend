package com.nistra.demy.platform.iam.interfaces.rest.resources;

public record VerifyResetPasswordCodeResource(
        String emailAddress,
        String code
) {
    public VerifyResetPasswordCodeResource {
        if (emailAddress == null || emailAddress.isBlank())
            throw new IllegalArgumentException("Email address cannot be null or blank");
        if (code == null || code.isBlank())
            throw new IllegalArgumentException("Reset code cannot be null or blank");
    }
}
