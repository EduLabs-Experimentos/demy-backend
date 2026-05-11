package com.nistra.demy.platform.institution.domain.services;

import com.nistra.demy.platform.institution.domain.model.aggregates.Academy;
import com.nistra.demy.platform.institution.domain.model.queries.ExistsAcademyByIdQuery;
import com.nistra.demy.platform.institution.domain.model.queries.GetAcademyByIdQuery;
import com.nistra.demy.platform.institution.domain.model.queries.GetCurrentAcademyQuery;

import java.util.Optional;

public interface AcademyQueryService {

    Optional<Academy> handle(GetAcademyByIdQuery query);

    boolean handle(ExistsAcademyByIdQuery query);

    Optional<Academy> handle(GetCurrentAcademyQuery query);
}
