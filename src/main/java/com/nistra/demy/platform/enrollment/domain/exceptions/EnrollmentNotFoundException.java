package com.nistra.demy.platform.enrollment.domain.exceptions;

import com.nistra.demy.platform.shared.domain.exceptions.DomainException;

public class EnrollmentNotFoundException extends DomainException {
    public EnrollmentNotFoundException(Long enrollmentId) {
        super("Enrollment with id %s not found or does not belong to the current academy context."
                        .formatted(enrollmentId),
                enrollmentId);
    }
}