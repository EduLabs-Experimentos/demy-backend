package com.nistra.demy.platform.scheduling.domain.exceptions;

import com.nistra.demy.platform.shared.domain.exceptions.DomainException;

public class ScheduleTeacherNotFoundException extends DomainException {
    public ScheduleTeacherNotFoundException(String firstName, String lastName) {
        super("Teacher with full name %s %s not found or not registered.".formatted(firstName, lastName), firstName, lastName);
    }
}