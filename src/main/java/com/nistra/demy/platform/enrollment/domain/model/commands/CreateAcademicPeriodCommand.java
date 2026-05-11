package com.nistra.demy.platform.enrollment.domain.model.commands;

import com.nistra.demy.platform.shared.domain.model.valueobjects.AcademyId;

import java.time.LocalDate;

/**
 * Command to create a new academic period.
 */
public record CreateAcademicPeriodCommand (String periodName, LocalDate startDate, LocalDate endDate) {
    public CreateAcademicPeriodCommand {
        if (periodName == null || periodName.isBlank()) {
            throw new IllegalArgumentException("title cannot be null or blank");
        }
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("startDate and endDate must not be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("startDate must be after endDate");
        }
    }
}
