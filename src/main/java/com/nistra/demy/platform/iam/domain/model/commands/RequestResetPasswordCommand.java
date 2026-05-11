package com.nistra.demy.platform.iam.domain.model.commands;

import com.nistra.demy.platform.shared.domain.model.valueobjects.EmailAddress;

public record RequestResetPasswordCommand(
        EmailAddress emailAddress
) {
    public RequestResetPasswordCommand {
        if (emailAddress == null)
            throw new IllegalArgumentException("Email address cannot be null");
    }
}
