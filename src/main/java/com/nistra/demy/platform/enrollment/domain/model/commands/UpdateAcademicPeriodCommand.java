package com.nistra.demy.platform.enrollment.domain.model.commands;

import java.time.LocalDate;

/**
 * Update Academic Period Name Command
 */
public record UpdateAcademicPeriodCommand(Long academicPeriodId, String periodName, LocalDate startDate, LocalDate endDate, Boolean isActive) {
    public UpdateAcademicPeriodCommand {
        if (academicPeriodId == null || academicPeriodId < 1) {
            throw new IllegalArgumentException("academicPeriodId cannot be null or less than 1");
        }
        if (periodName == null || periodName.isBlank()) {
            throw new IllegalArgumentException("periodName cannot be null or empty");
        }
        if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("startDate cannot be null or less than endDate");
        }
    }
}