package com.nistra.demy.platform.enrollment.interfaces.rest.transform;

import com.nistra.demy.platform.enrollment.domain.model.commands.CreateStudentCommand;
import com.nistra.demy.platform.enrollment.domain.model.valueobjects.Sex;
import com.nistra.demy.platform.enrollment.interfaces.rest.resources.CreateStudentResource;
import com.nistra.demy.platform.shared.domain.model.valueobjects.*;

public class CreateStudentCommandFromResourceAssembler {
    public static CreateStudentCommand toCommandFromResource (CreateStudentResource resource) {
        return new CreateStudentCommand(
                new PersonName(
                        resource.firstName(),
                        resource.lastName()
                ),
                new DniNumber(
                        resource.dni()
                ),
                new EmailAddress(
                        resource.emailAddress()
                ),
                Sex.valueOf(
                        resource.sex().toUpperCase()
                ),
                resource.birthDate(),
                new StreetAddress(
                        resource.street(),
                        resource.province(),
                        resource.department(),
                        resource.department()
                ),
                new PhoneNumber(
                        resource.countryCode(),
                        resource.phone()
                )
        );
    }
}
