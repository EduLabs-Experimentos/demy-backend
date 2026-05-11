package com.nistra.demy.platform.enrollment.domain.model.queries;

public record GetStudentByIdQuery(Long studentId) {
    public GetStudentByIdQuery {
        if (studentId == null || studentId < 1) {
            throw new IllegalArgumentException("studentId must be greater than 0");
        }
    }
}
