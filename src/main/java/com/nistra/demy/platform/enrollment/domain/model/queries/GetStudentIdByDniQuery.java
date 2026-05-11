package com.nistra.demy.platform.enrollment.domain.model.queries;

import com.nistra.demy.platform.shared.domain.model.valueobjects.DniNumber;

public record GetStudentIdByDniQuery(
        DniNumber dniNumber
) {
    public GetStudentIdByDniQuery {
        if (dniNumber == null) {
            throw new IllegalArgumentException("DNI number cannot be null");
        }
    }
}
