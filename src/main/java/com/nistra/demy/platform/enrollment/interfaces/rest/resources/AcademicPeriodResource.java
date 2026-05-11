package com.nistra.demy.platform.enrollment.interfaces.rest.resources;

import com.nistra.demy.platform.shared.domain.model.valueobjects.AcademyId;

import java.time.LocalDate;

public record AcademicPeriodResource (
        Long id,
        String periodName,
        LocalDate startDate,
        LocalDate endDate,
        Boolean isActive,
        Long academyId) {
}
