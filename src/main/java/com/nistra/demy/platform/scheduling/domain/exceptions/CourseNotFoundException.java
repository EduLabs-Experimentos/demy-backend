package com.nistra.demy.platform.scheduling.domain.exceptions;

import com.nistra.demy.platform.shared.domain.exceptions.DomainException;

public class CourseNotFoundException extends DomainException {
    public CourseNotFoundException(Long courseId) {
        super("Course with id %s not found or does not belong to the current academy context.".formatted(courseId), courseId);
    }
}