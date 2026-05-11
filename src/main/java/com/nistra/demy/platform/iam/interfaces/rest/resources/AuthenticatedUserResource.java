package com.nistra.demy.platform.iam.interfaces.rest.resources;

public record AuthenticatedUserResource(
        Long id,
        String emailAddress,
        String token
) {
}
