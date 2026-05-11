package com.nistra.demy.platform.iam.interfaces.rest.resources;

import java.util.List;

public record SignUpResource(
        String emailAddress,
        String password,
        List<String> roles
) {
}
