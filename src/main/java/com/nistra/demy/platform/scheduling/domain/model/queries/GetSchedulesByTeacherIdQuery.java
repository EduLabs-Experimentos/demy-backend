package com.nistra.demy.platform.scheduling.domain.model.queries;

import com.nistra.demy.platform.institution.domain.model.valueobjects.UserId;

public record GetSchedulesByTeacherIdQuery (UserId teacherId) {
    public GetSchedulesByTeacherIdQuery{
        if (teacherId == null || teacherId.userId() <= 0) {
            throw new IllegalArgumentException("TeacherId cannot be null or less than 1");
        }
    }
}
