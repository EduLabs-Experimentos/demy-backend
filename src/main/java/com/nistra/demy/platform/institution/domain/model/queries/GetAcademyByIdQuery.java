package com.nistra.demy.platform.institution.domain.model.queries;

public record GetAcademyByIdQuery(Long academyId) {

    public GetAcademyByIdQuery {
        if (academyId == null || academyId <= 0)
            throw new IllegalArgumentException("AcademyId cannot be null or less than or equal to zero");
    }
}
