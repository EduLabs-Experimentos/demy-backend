package com.nistra.demy.platform.institution.interfaces.rest.transform;

import com.nistra.demy.platform.institution.domain.model.commands.RegisterTeacherCommand;
import com.nistra.demy.platform.institution.interfaces.rest.resources.RegisterTeacherResource;
import com.nistra.demy.platform.shared.domain.model.valueobjects.EmailAddress;
import com.nistra.demy.platform.shared.domain.model.valueobjects.PersonName;
import com.nistra.demy.platform.shared.domain.model.valueobjects.PhoneNumber;

public class RegisterTeacherCommandFromResourceAssembler {
    public static RegisterTeacherCommand toCommandFromResource(RegisterTeacherResource resource) {
        return new RegisterTeacherCommand(
                new PersonName(
                        resource.firstName(),
                        resource.lastName()
                ),
                new EmailAddress(resource.emailAddress()),
                new PhoneNumber(
                        resource.countryCode(),
                        resource.phone()
                )
        );
    }
}
