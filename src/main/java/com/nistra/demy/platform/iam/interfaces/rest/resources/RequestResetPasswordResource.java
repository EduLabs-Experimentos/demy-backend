package com.nistra.demy.platform.iam.interfaces.rest.resources;

public record RequestResetPasswordResource(
        String emailAddress
) {
    public RequestResetPasswordResource {
        if (emailAddress == null || emailAddress.isBlank())
            throw new IllegalArgumentException("Email address cannot be null or blank");
    }
}
