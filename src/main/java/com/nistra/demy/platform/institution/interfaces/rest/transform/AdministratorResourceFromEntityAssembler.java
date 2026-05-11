package com.nistra.demy.platform.institution.interfaces.rest.transform;

import com.nistra.demy.platform.institution.domain.model.aggregates.Administrator;
import com.nistra.demy.platform.institution.interfaces.rest.resources.AdministratorResource;

public class AdministratorResourceFromEntityAssembler {
    public static AdministratorResource toResourceFromEntity(Administrator entity) {
        return new AdministratorResource(
                entity.getId(),
                entity.getPersonName().firstName(),
                entity.getPersonName().lastName(),
                entity.getPhoneNumber().getFullNumber(),
                entity.getDniNumber().dniNumber(),
                entity.getAcademyId().academyId(),
                entity.getUserId().userId()
        );
    }
}
