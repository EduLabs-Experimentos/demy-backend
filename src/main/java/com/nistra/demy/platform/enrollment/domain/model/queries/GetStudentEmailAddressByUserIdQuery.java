package com.nistra.demy.platform.enrollment.domain.model.queries;

import com.nistra.demy.platform.enrollment.domain.model.valueobjects.UserId;

public record GetStudentEmailAddressByUserIdQuery(UserId userId) {
    public GetStudentEmailAddressByUserIdQuery {
        if (userId == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }
    }
}
