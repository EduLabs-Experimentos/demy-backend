package com.nistra.demy.platform.institution.interfaces.rest.transform;

import com.nistra.demy.platform.institution.domain.model.aggregates.Teacher;
import com.nistra.demy.platform.institution.interfaces.rest.resources.CurrentTeacherResource;
import com.nistra.demy.platform.shared.domain.model.valueobjects.EmailAddress;

public class CurrentTeacherResourceFromEntityAssembler {
    public static CurrentTeacherResource toResourceFromEntity(Teacher entity, EmailAddress emailAddress) {
        return new CurrentTeacherResource(
                entity.getId(),
                entity.getPersonName().firstName(),
                entity.getPersonName().lastName(),
                emailAddress.email(),
                entity.getPhoneNumber().getFullNumber(),
                entity.getUserId().userId()
        );
    }
}
