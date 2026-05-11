package com.nistra.demy.platform.enrollment.interfaces.rest.resources;

import java.time.LocalDate;

/**
 * Update academic period resource.
 */
public record UpdateAcademicPeriodResource(
        String periodName,
        LocalDate startDate,
        LocalDate endDate,
        Boolean isActive) {
}
