package com.nistra.demy.platform.enrollment.domain.model.queries;

public record GetAcademicPeriodByIdQuery(Long academicPeriodId) {
    public GetAcademicPeriodByIdQuery {
        if (academicPeriodId == null || academicPeriodId < 1) {
            throw new IllegalArgumentException("AcademicPeriodId must be greater than 0 or not null");
        }
    }
}
