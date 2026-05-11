package com.nistra.demy.platform.enrollment.domain.model.queries;

import com.nistra.demy.platform.enrollment.domain.model.valueobjects.StudentId;

public record GetAllEnrollmentsByStudentIdQuery(StudentId studentId) {
    public GetAllEnrollmentsByStudentIdQuery {
        if (studentId == null) {
            throw new IllegalArgumentException("studentId cannot be null");
        }
    }
}
