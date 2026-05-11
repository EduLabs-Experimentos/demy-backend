package com.nistra.demy.platform.institution.domain.model.commands;

import com.nistra.demy.platform.institution.domain.model.valueobjects.UserId;
import com.nistra.demy.platform.shared.domain.model.valueobjects.*;

public record RegisterAdministratorCommand(
        PersonName personName,
        PhoneNumber phoneNumber,
        DniNumber dniNumber,
        UserId userId
) {
    public RegisterAdministratorCommand {
        if (personName == null)
            throw new IllegalArgumentException("Person name cannot be null");
        if (phoneNumber == null)
            throw new IllegalArgumentException("Phone number cannot be null");
        if (dniNumber == null)
            throw new IllegalArgumentException("DNI number cannot be null");
        if (userId == null)
            throw new IllegalArgumentException("User ID cannot be null");
    }
}
