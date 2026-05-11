package com.nistra.demy.platform.enrollment.domain.exceptions;

import com.nistra.demy.platform.shared.domain.exceptions.DomainException;

public class StudentNotFoundException extends DomainException {
    public StudentNotFoundException(Long studentId) {
        super("Student with id %s not found or does not belong to the current academy context.".formatted(studentId), studentId);
    }
}