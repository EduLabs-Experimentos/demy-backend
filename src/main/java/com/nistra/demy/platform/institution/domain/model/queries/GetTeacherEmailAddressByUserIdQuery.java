package com.nistra.demy.platform.institution.domain.model.queries;

import com.nistra.demy.platform.institution.domain.model.valueobjects.UserId;

public record GetTeacherEmailAddressByUserIdQuery(UserId userId) {
    public GetTeacherEmailAddressByUserIdQuery {
        if (userId == null)
            throw new IllegalArgumentException("UserId cannot be null");
    }
}
