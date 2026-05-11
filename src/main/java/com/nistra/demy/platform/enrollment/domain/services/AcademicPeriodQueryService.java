package com.nistra.demy.platform.enrollment.domain.services;

import com.nistra.demy.platform.enrollment.domain.model.aggregates.AcademicPeriod;
import com.nistra.demy.platform.enrollment.domain.model.queries.GetAcademicPeriodByIdQuery;
import com.nistra.demy.platform.enrollment.domain.model.queries.GetAllAcademicPeriodsQuery;

import java.util.List;
import java.util.Optional;

public interface AcademicPeriodQueryService {
    Optional<AcademicPeriod> handle(GetAcademicPeriodByIdQuery query);
    List<AcademicPeriod> handle(GetAllAcademicPeriodsQuery query);
}
