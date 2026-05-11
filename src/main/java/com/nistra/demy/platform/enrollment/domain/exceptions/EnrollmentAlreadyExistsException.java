package com.nistra.demy.platform.enrollment.domain.exceptions;

import com.nistra.demy.platform.enrollment.domain.model.valueobjects.PeriodId;
import com.nistra.demy.platform.enrollment.domain.model.valueobjects.StudentId;
import com.nistra.demy.platform.shared.domain.exceptions.DomainException;

public class EnrollmentAlreadyExistsException extends DomainException {
    public EnrollmentAlreadyExistsException(StudentId studentId, PeriodId periodId) {
        super("Enrollment already exists for student %s in period %s within the current academy context."
                        .formatted(studentId.studentId(), periodId.periodId()),
                studentId.studentId(), periodId.periodId());
    }
}