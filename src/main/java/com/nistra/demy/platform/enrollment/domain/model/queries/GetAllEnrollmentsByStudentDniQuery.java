package com.nistra.demy.platform.enrollment.domain.model.queries;

import com.nistra.demy.platform.shared.domain.model.valueobjects.DniNumber;

public record GetAllEnrollmentsByStudentDniQuery(DniNumber dni) {
    public GetAllEnrollmentsByStudentDniQuery {
        if (dni == null) {
            throw new IllegalArgumentException("Dni cannot be null");
        }
    }
}
