package com.nistra.demy.platform.institution.domain.exceptions;

import com.nistra.demy.platform.shared.domain.exceptions.DomainException;
import com.nistra.demy.platform.shared.domain.model.valueobjects.DniNumber;

public class AdministratorAlreadyExistsException extends DomainException {
    public AdministratorAlreadyExistsException(DniNumber dni) {
        super("Administrator already exists with DNI %s".formatted(dni.dniNumber()), dni.dniNumber());
    }
}
