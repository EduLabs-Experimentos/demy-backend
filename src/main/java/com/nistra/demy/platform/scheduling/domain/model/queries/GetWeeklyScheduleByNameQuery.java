package com.nistra.demy.platform.scheduling.domain.model.queries;

public record GetWeeklyScheduleByNameQuery(String name) {
    public GetWeeklyScheduleByNameQuery{
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
    }
}
