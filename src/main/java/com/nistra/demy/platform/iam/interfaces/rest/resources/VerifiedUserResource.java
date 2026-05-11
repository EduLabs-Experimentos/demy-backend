package com.nistra.demy.platform.iam.interfaces.rest.resources;

public record VerifiedUserResource(
        Long id,
        String emailAddress,
        String token
) {
}
