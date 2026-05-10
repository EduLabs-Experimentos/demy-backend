package com.nistra.demy.platform.institution.interfaces.rest.transform;

import com.nistra.demy.platform.institution.domain.model.aggregates.Academy;
import com.nistra.demy.platform.institution.interfaces.rest.resources.CurrentAcademyResource;

public class CurrentAcademyResourceFromEntityAssembler {
    public static CurrentAcademyResource toResourceFromEntity(Academy entity) {
        return new CurrentAcademyResource(
                entity.getAcademyName().name(),
                entity.getStreetAddress().street(),
                entity.getStreetAddress().district(),
                entity.getStreetAddress().province(),
                entity.getStreetAddress().department(),
                entity.getPhoneNumber().getFullNumber(),
                entity.getEmailAddress().email(),
                entity.getRuc().ruc()
        );
    }
}
