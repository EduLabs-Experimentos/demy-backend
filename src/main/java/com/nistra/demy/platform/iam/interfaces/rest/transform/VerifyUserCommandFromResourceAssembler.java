package com.nistra.demy.platform.iam.interfaces.rest.transform;

import com.nistra.demy.platform.iam.domain.model.commands.VerifyUserCommand;
import com.nistra.demy.platform.iam.interfaces.rest.resources.VerifyUserResource;

public class VerifyUserCommandFromResourceAssembler {
    public static VerifyUserCommand toCommandFromResource(VerifyUserResource resource) {
        return new VerifyUserCommand(
                resource.email(),
                resource.code()
        );
    }
}
