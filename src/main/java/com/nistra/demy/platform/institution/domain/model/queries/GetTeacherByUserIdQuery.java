package com.nistra.demy.platform.institution.domain.model.queries;

import com.nistra.demy.platform.institution.domain.model.valueobjects.UserId;

public record GetTeacherByUserIdQuery(UserId userId) {
    public GetTeacherByUserIdQuery {
        if (userId == null || userId.userId() <= 0) {
            throw new IllegalArgumentException("UserId cannot be null or less than 1");
        }
    }
}