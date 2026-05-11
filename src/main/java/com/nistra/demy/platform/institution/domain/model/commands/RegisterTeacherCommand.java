package com.nistra.demy.platform.institution.domain.model.commands;

import com.nistra.demy.platform.shared.domain.model.valueobjects.EmailAddress;
import com.nistra.demy.platform.shared.domain.model.valueobjects.PersonName;
import com.nistra.demy.platform.shared.domain.model.valueobjects.PhoneNumber;

public record RegisterTeacherCommand(
        PersonName personName,
        EmailAddress emailAddress,
        PhoneNumber phoneNumber
) {
    public RegisterTeacherCommand {
        if (personName == null)
            throw new IllegalArgumentException("Person name cannot be null");
        if (emailAddress == null)
            throw new IllegalArgumentException("Email address cannot be null");
        if (phoneNumber == null)
            throw new IllegalArgumentException("Phone number cannot be null");
    }
}
