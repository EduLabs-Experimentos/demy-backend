package com.nistra.demy.platform.scheduling.domain.exceptions;

import com.nistra.demy.platform.shared.domain.exceptions.DomainException;

public class ClassroomNotFoundException extends DomainException {
    public ClassroomNotFoundException(Long classroomId) {
        super("Classroom with id %s not found or does not belong to the current academy context.".formatted(classroomId), classroomId);
    }
}