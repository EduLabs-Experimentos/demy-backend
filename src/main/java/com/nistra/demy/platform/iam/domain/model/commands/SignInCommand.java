package com.nistra.demy.platform.iam.domain.model.commands;

import com.nistra.demy.platform.shared.domain.model.valueobjects.EmailAddress;

public record SignInCommand(
        EmailAddress emailAddress,
        String password
) {
}
