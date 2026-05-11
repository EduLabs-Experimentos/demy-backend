package com.nistra.demy.platform.institution.domain.model.queries;

public record ExistsAcademyByIdQuery(Long academyId) {
    public ExistsAcademyByIdQuery {
        if (academyId == null || academyId <= 0)
            throw new IllegalArgumentException("AcademyId cannot be null or less than or equal to zero");
    }
}
