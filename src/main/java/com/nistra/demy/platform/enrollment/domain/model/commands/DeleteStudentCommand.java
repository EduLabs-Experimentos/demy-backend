package com.nistra.demy.platform.enrollment.domain.model.commands;

public record DeleteStudentCommand(Long studentId) {
    public DeleteStudentCommand {
        if (studentId == null || studentId < 1){
            throw new IllegalArgumentException("studentId must be greater than 0");
        }
    }
}
