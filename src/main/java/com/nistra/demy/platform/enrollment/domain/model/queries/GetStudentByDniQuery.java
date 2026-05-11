package com.nistra.demy.platform.enrollment.domain.model.queries;

import com.nistra.demy.platform.shared.domain.model.valueobjects.DniNumber;

public record GetStudentByDniQuery(DniNumber dniNumber) {
    public GetStudentByDniQuery {
        if (dniNumber == null) {
            throw new IllegalArgumentException("dni cant be null");
        }
    }
}
