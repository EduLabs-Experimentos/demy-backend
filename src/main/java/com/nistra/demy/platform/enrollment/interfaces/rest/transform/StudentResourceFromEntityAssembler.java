package com.nistra.demy.platform.enrollment.interfaces.rest.transform;

import com.nistra.demy.platform.enrollment.domain.model.aggregates.Student;
import com.nistra.demy.platform.enrollment.interfaces.rest.resources.StudentResource;
import com.nistra.demy.platform.shared.domain.model.valueobjects.EmailAddress;

public class StudentResourceFromEntityAssembler {
    public static StudentResource toResourceFromEntity(Student entity, EmailAddress emailAddress) {
        return new StudentResource(
                entity.getId(),
                entity.getPersonName().firstName(),
                entity.getPersonName().lastName(),
                entity.getDni().dniNumber(),
                emailAddress.email(),
                entity.getSex().toString(),
                entity.getBirthDate(),
                entity.getAddress().street(),
                entity.getAddress().district(),
                entity.getAddress().province(),
                entity.getAddress().department(),
                entity.getPhoneNumber().countryCode(),
                entity.getPhoneNumber().phone(),
                entity.getAcademyId().academyId(),
                entity.getUserId().userId()
                );
    }
}
