package com.nistra.demy.platform.iam.interfaces.rest.transform;

import com.nistra.demy.platform.iam.domain.model.commands.RequestResetPasswordCommand;
import com.nistra.demy.platform.iam.interfaces.rest.resources.RequestResetPasswordResource;
import com.nistra.demy.platform.shared.domain.model.valueobjects.EmailAddress;

public class RequestResetPasswordCommandFromResourceAssembler {
    public static RequestResetPasswordCommand toCommandFromResource(RequestResetPasswordResource resource) {
        return new RequestResetPasswordCommand(
                new EmailAddress(resource.emailAddress())
        );
    }
}
