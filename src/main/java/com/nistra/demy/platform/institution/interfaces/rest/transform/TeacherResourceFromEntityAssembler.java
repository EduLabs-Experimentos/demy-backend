package com.nistra.demy.platform.institution.interfaces.rest.transform;

import com.nistra.demy.platform.institution.domain.model.aggregates.Teacher;
import com.nistra.demy.platform.institution.interfaces.rest.resources.TeacherResource;
import com.nistra.demy.platform.shared.domain.model.valueobjects.EmailAddress;
import org.springframework.stereotype.Component;

@Component
public class TeacherResourceFromEntityAssembler {
    public static TeacherResource toResourceFromEntity(Teacher entity, EmailAddress emailAddress) {
        return new TeacherResource(
                entity.getId(),
                entity.getPersonName().firstName(),
                entity.getPersonName().lastName(),
                emailAddress.email(),
                entity.getPhoneNumber().countryCode(),
                entity.getPhoneNumber().phone(),
                entity.getAcademyId().academyId()
        );
    }
}