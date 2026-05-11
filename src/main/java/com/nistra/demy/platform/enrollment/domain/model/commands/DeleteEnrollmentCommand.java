package com.nistra.demy.platform.enrollment.domain.model.commands;

public record DeleteEnrollmentCommand(Long enrollmentId) {
    public DeleteEnrollmentCommand {
        if (enrollmentId == null || enrollmentId < 1){
            throw new IllegalArgumentException("enrollmentId must be greater than 0");
        }
    }
}
