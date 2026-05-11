package com.nistra.demy.platform.institution.domain.model.queries;

import com.nistra.demy.platform.shared.domain.model.valueobjects.DniNumber;

public record GetAdministratorByDniNumberQuery(DniNumber dniNumber) {

    public GetAdministratorByDniNumberQuery {
        if (dniNumber == null)
            throw new IllegalArgumentException("DNI number is required");
    }
}
