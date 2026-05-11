package com.nistra.demy.platform.enrollment.interfaces.rest.transform;

import com.nistra.demy.platform.enrollment.domain.model.commands.UpdateStudentCommand;
import com.nistra.demy.platform.enrollment.domain.model.valueobjects.Sex;
import com.nistra.demy.platform.enrollment.interfaces.rest.resources.UpdateStudentResource;
import com.nistra.demy.platform.shared.domain.model.valueobjects.DniNumber;
import com.nistra.demy.platform.shared.domain.model.valueobjects.PersonName;
import com.nistra.demy.platform.shared.domain.model.valueobjects.PhoneNumber;
import com.nistra.demy.platform.shared.domain.model.valueobjects.StreetAddress;

public class UpdateStudentCommandFromResourceAssembler {
    public static UpdateStudentCommand toCommandFromResource(Long studentId, UpdateStudentResource resource) {
        return new UpdateStudentCommand(
                studentId,
                new PersonName(
                        resource.firstName(),
                        resource.lastName()
                ),
                new DniNumber(
                   resource.dni()
                ),
                Sex.valueOf(
                        resource.sex().toUpperCase()
                ),
                resource.birthDate(),
                new StreetAddress(
                        resource.street(),
                        resource.district(),
                        resource.province(),
                        resource.department()
                ),
                new PhoneNumber(
                        resource.countryCode(),
                        resource.phoneNumber()
                )
        );
    }
}
