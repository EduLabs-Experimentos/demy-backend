package com.nistra.demy.platform.institution.interfaces.rest.transform;

import com.nistra.demy.platform.institution.domain.model.aggregates.Administrator;
import com.nistra.demy.platform.institution.interfaces.rest.resources.CurrentAdministratorResource;
import com.nistra.demy.platform.shared.domain.model.valueobjects.EmailAddress;

public class CurrentAdministratorResourceFromEntityAssembler {
    public static CurrentAdministratorResource toResourceFromEntity(Administrator entity, EmailAddress emailAddress) {
        return new CurrentAdministratorResource(
                entity.getPersonName().firstName(),
                entity.getPersonName().lastName(),
                emailAddress.email(),
                entity.getPhoneNumber().getFullNumber(),
                entity.getDniNumber().dniNumber()
        );
    }
}
