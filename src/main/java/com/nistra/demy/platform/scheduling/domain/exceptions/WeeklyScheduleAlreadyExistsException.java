package com.nistra.demy.platform.scheduling.domain.exceptions;

import com.nistra.demy.platform.shared.domain.exceptions.DomainException;

public class WeeklyScheduleAlreadyExistsException extends DomainException {
    public WeeklyScheduleAlreadyExistsException(String name) {
        super("Weekly schedule %s already exists.".formatted(name), name);
    }
}