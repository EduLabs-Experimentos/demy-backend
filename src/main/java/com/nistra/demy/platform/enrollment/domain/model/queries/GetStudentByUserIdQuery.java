package com.nistra.demy.platform.enrollment.domain.model.queries;

import com.nistra.demy.platform.enrollment.domain.model.valueobjects.UserId;

public record GetStudentByUserIdQuery(UserId userId) {
    public GetStudentByUserIdQuery {
        if (userId == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }
    }
}