package com.nistra.demy.platform.iam.interfaces.rest.transform;

import com.nistra.demy.platform.iam.domain.model.aggregates.User;
import com.nistra.demy.platform.iam.interfaces.rest.resources.ResetPasswordResponseResource;

public class ResetPasswordResponseResourceFromEntityAssembler {
    public static ResetPasswordResponseResource toResourceFromEntity(User user, String token) {
        return new ResetPasswordResponseResource(
                user.getId(),
                user.getEmailAddress().email(),
                token
        );
    }
}

