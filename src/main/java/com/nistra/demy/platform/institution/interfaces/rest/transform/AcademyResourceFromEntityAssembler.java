package com.nistra.demy.platform.institution.interfaces.rest.transform;

import com.nistra.demy.platform.institution.domain.model.aggregates.Academy;
import com.nistra.demy.platform.institution.interfaces.rest.resources.AcademyResource;

public class AcademyResourceFromEntityAssembler {
    public static AcademyResource toResourceFromEntity(Academy entity) {
        return new AcademyResource(
                entity.getId(),
                entity.getAdministratorId().administratorId(),
                entity.getAcademyName().name(),
                entity.getAcademyDescription().description(),
                entity.getStreetAddress().street(),
                entity.getStreetAddress().district(),
                entity.getStreetAddress().province(),
                entity.getStreetAddress().department(),
                entity.getEmailAddress().email(),
                entity.getPhoneNumber().getFullNumber(),
                entity.getRuc().ruc()
        );
    }
}
