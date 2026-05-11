package com.nistra.demy.platform.enrollment.interfaces.rest.resources;

import com.nistra.demy.platform.shared.domain.model.valueobjects.AcademyId;

import java.time.LocalDate;

/**
 * Create academic period resource.
 */
public record CreateAcademicPeriodResource(
        String periodName,
        LocalDate startDate,
        LocalDate endDate) {
}