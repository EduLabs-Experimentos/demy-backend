package com.nistra.demy.platform.institution.interfaces.rest.transform;

import com.nistra.demy.platform.institution.domain.model.commands.RegisterAcademyCommand;
import com.nistra.demy.platform.institution.domain.model.valueobjects.AcademyDescription;
import com.nistra.demy.platform.institution.domain.model.valueobjects.AcademyName;
import com.nistra.demy.platform.institution.domain.model.valueobjects.AdministratorId;
import com.nistra.demy.platform.institution.domain.model.valueobjects.Ruc;
import com.nistra.demy.platform.institution.interfaces.rest.resources.RegisterAcademyResource;
import com.nistra.demy.platform.shared.domain.model.valueobjects.EmailAddress;
import com.nistra.demy.platform.shared.domain.model.valueobjects.PhoneNumber;
import com.nistra.demy.platform.shared.domain.model.valueobjects.StreetAddress;

public class RegisterAcademyCommandFromResourceAssembler {
    public static RegisterAcademyCommand toCommandFromResource(RegisterAcademyResource resource) {
        return new RegisterAcademyCommand(
                new AcademyName(resource.academyName()),
                new AcademyDescription(resource.academyDescription()),
                new StreetAddress(
                        resource.street(),
                        resource.district(),
                        resource.province(),
                        resource.department()
                ),
                new EmailAddress(resource.emailAddress()),
                new PhoneNumber(
                        resource.countryCode(),
                        resource.phone()
                ),
                new Ruc(resource.ruc()),
                new AdministratorId(resource.administratorId())
        );
    }
}
