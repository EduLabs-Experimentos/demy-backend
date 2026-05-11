package com.nistra.demy.platform.iam.interfaces.rest.resources;

public record ResetPasswordResponseResource(
        Long id,
        String emailAddress,
        String token
) {
}

