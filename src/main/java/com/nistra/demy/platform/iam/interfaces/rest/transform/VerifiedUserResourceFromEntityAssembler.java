package com.nistra.demy.platform.iam.interfaces.rest.transform;

import com.nistra.demy.platform.iam.domain.model.aggregates.User;
import com.nistra.demy.platform.iam.interfaces.rest.resources.VerifiedUserResource;

public class VerifiedUserResourceFromEntityAssembler {
    public static VerifiedUserResource toResourceFromEntity(User user, String token) {
        return new VerifiedUserResource(user.getId(), user.getEmailAddress().email(), token);
    }
}
