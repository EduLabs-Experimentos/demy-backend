package com.nistra.demy.platform.enrollment.domain.model.commands;

public record DeleteAcademicPeriodCommand(Long academicPeriodId) {
    public DeleteAcademicPeriodCommand {
        if (academicPeriodId == null || academicPeriodId <= 0) {
            throw new IllegalArgumentException("courseId cannot be null or less than 1");
        }
    }
}
