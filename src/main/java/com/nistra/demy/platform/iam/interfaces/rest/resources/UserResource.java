package com.nistra.demy.platform.iam.interfaces.rest.resources;

import java.util.List;

public record UserResource(
        Long id,
        String emailAddress,
        List<String> roles,
        Long tenantId
) {
}
