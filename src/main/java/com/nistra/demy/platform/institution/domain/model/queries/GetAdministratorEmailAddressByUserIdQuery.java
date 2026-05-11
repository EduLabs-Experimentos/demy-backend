package com.nistra.demy.platform.institution.domain.model.queries;

import com.nistra.demy.platform.institution.domain.model.valueobjects.UserId;

public record GetAdministratorEmailAddressByUserIdQuery(
        UserId userId
) {
    public GetAdministratorEmailAddressByUserIdQuery {
        if (userId == null)
            throw new IllegalArgumentException("User ID cannot be null");
    }
}
