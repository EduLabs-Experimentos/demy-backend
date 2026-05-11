package com.nistra.demy.platform.scheduling.domain.exceptions;

import com.nistra.demy.platform.shared.domain.exceptions.DomainException;

public class CourseAlreadyExistsException extends DomainException {
    public CourseAlreadyExistsException(String name) {
        super("Course with name %s already exists.".formatted(name), name);
    }
}