package com.nistra.demy.platform.scheduling.domain.exceptions;

import com.nistra.demy.platform.shared.domain.exceptions.DomainException;

public class ClassroomAlreadyExistsException extends DomainException {
    public ClassroomAlreadyExistsException(String code) {
        super("Classroom with code %s already exists.".formatted(code), code);
    }
}