package com.nistra.demy.platform.iam.interfaces.rest.transform;

import com.nistra.demy.platform.iam.domain.model.commands.ResetPasswordCommand;
import com.nistra.demy.platform.iam.interfaces.rest.resources.ResetPasswordResource;
import com.nistra.demy.platform.shared.domain.model.valueobjects.EmailAddress;

public class ResetPasswordCommandFromResourceAssembler {
    public static ResetPasswordCommand toCommandFromResource(ResetPasswordResource resource) {
        return new ResetPasswordCommand(
                new EmailAddress(resource.emailAddress()),
                resource.password()
        );
    }
}
