package com.nistra.demy.platform.scheduling.domain.model.queries;

public record GetWeeklyScheduleByIdQuery(Long weeklyScheduleId) {

    public GetWeeklyScheduleByIdQuery{
        if (weeklyScheduleId == null || weeklyScheduleId <= 0) {
            throw new IllegalArgumentException("WeeklyScheduleId cannot be null or less than 1");
        }
    }
}