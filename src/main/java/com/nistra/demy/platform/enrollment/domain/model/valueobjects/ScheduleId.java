package com.nistra.demy.platform.enrollment.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record ScheduleId(Long scheduleId) {
        public ScheduleId {
        if (scheduleId == null || scheduleId < 1) {
            throw new IllegalArgumentException("Weekly schedule ID cant be null or must be greater than 0");
        }
    }
}
