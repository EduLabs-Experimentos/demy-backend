package com.nistra.demy.platform.institution.domain.model.commands;

import com.nistra.demy.platform.institution.domain.model.valueobjects.AcademyDescription;
import com.nistra.demy.platform.institution.domain.model.valueobjects.AcademyName;
import com.nistra.demy.platform.institution.domain.model.valueobjects.AdministratorId;
import com.nistra.demy.platform.institution.domain.model.valueobjects.Ruc;
import com.nistra.demy.platform.shared.domain.model.valueobjects.EmailAddress;
import com.nistra.demy.platform.shared.domain.model.valueobjects.PhoneNumber;
import com.nistra.demy.platform.shared.domain.model.valueobjects.StreetAddress;

/**
 * Command to register a new academy in the system.
 * <p>
 * Encapsulates the required data for registering an academy,
 * including the administrator's identifier and the academy's RUC.
 *
 * @author Salim Ramirez
 * @see IllegalArgumentException
 * @since 1.0.0
 */
public record RegisterAcademyCommand(
        AcademyName academyName,
        AcademyDescription academyDescription,
        StreetAddress streetAddress,
        EmailAddress emailAddress,
        PhoneNumber phoneNumber,
        Ruc ruc,
        AdministratorId administratorId
) {
    /**
     * Constructs a RegisterAcademyCommand with validation.
     *
     * @param academyName the name of the academy
     * @param academyDescription the description of the academy
     * @param streetAddress the street address of the academy
     * @param emailAddress the email address of the academy
     * @param phoneNumber the phone number of the academy
     * @param ruc the RUC of the academy
     * @throws IllegalArgumentException if administratorId or ruc is null
     */
    public RegisterAcademyCommand {
        if (academyName == null)
            throw new IllegalArgumentException("AcademyName cannot be null");
        if (academyDescription == null)
            throw new IllegalArgumentException("AcademyDescription cannot be null");
        if (streetAddress == null)
            throw new IllegalArgumentException("StreetAddress cannot be null");
        if (emailAddress == null)
            throw new IllegalArgumentException("EmailAddress cannot be null");
        if (phoneNumber == null)
            throw new IllegalArgumentException("PhoneNumber cannot be null");
        if (ruc == null)
            throw new IllegalArgumentException("Ruc cannot be null");
        if (administratorId == null)
            throw new IllegalArgumentException("AdministratorId cannot be null");
    }
}
