package com.nistra.demy.platform.scheduling.domain.exceptions;

import com.nistra.demy.platform.shared.domain.exceptions.DomainException;

public class WeeklyScheduleNotFoundException extends DomainException {
    public WeeklyScheduleNotFoundException(Long weeklyScheduleId) {
        super("Weekly schedule with id %s not found or does not belong to the current academy context.".formatted(weeklyScheduleId), weeklyScheduleId);
    }
}