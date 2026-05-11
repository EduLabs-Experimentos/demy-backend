package com.nistra.demy.platform.enrollment.domain.model.queries;

public record GetEnrollmentByIdQuery(Long enrollmentId) {
    public GetEnrollmentByIdQuery {
        if (enrollmentId == null || enrollmentId < 1) {
            throw new IllegalArgumentException("enrollmentId cannot be null or less than 1");
        }
    }
}
