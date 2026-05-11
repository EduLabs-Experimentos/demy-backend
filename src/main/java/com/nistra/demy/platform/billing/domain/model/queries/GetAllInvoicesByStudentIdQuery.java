package com.nistra.demy.platform.billing.domain.model.queries;

import com.nistra.demy.platform.shared.domain.model.valueobjects.StudentId;

public record GetAllInvoicesByStudentIdQuery(
        StudentId studentId
) {
    public GetAllInvoicesByStudentIdQuery {
        if (studentId == null)
            throw new IllegalArgumentException("Student ID must be a non-null value");
    }
}
