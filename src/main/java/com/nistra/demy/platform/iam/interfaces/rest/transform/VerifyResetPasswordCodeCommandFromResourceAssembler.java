package com.nistra.demy.platform.iam.interfaces.rest.transform;

import com.nistra.demy.platform.iam.domain.model.commands.VerifyResetPasswordCodeCommand;
import com.nistra.demy.platform.iam.interfaces.rest.resources.VerifyResetPasswordCodeResource;
import com.nistra.demy.platform.shared.domain.model.valueobjects.EmailAddress;

public class VerifyResetPasswordCodeCommandFromResourceAssembler {
    public static VerifyResetPasswordCodeCommand toCommandFromResource(VerifyResetPasswordCodeResource resource) {
        return new VerifyResetPasswordCodeCommand(
                new EmailAddress(resource.emailAddress()),
                resource.code()
        );
    }
}
