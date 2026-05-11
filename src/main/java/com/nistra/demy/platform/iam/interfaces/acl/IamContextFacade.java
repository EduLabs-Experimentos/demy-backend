package com.nistra.demy.platform.iam.interfaces.acl;

import java.util.List;

public interface IamContextFacade {
    String fetchUserEmailAddressByUserId(Long userId);

    Long signUpVerifiedUser(String email, List<String> roles);

    Long fetchAuthenticatedUserId();

    Long fetchAuthenticatedUserTenantId();
}
