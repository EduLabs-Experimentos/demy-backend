package com.nistra.demy.platform.iam.domain.model.commands;

import com.nistra.demy.platform.iam.domain.model.entities.Role;
import com.nistra.demy.platform.shared.domain.model.valueobjects.EmailAddress;

import java.util.List;

public record SignUpVerifiedUserCommand(
        EmailAddress emailAddress,
        String password,
        List<Role> roles
) {
}
