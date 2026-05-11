package com.nistra.demy.platform.scheduling.domain.model.commands;

/**
 * Command to create a new classroom with specific details such as code, capacity, and campus.
 *
 * @param code The unique identifier for the classroom.
 * @param capacity The maximum number of students the classroom can accommodate.
 * @param campus The campus where the classroom is located.
 *
 */
public record CreateClassroomCommand(String code, Integer capacity, String campus) {

    public CreateClassroomCommand {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Code must not be null or blank");
        }
        if (capacity == null || capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0");
        }
        if (campus == null || campus.isBlank()) {
            throw new IllegalArgumentException("Campus must not be null or blank");
        }
    }
}
