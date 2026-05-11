package com.nistra.demy.platform.scheduling.domain.model.commands;

/**
 * Command to update the details of an existing classroom.
 *
 * @param classroomId The unique identifier of the classroom to be updated.
 * @param code The new code for the classroom.
 * @param capacity The updated capacity of the classroom.
 * @param campus The updated campus where the classroom is located.
 */
public record UpdateClassroomCommand(
        Long classroomId,
        String code,
        Integer capacity,
        String campus
) {
    public UpdateClassroomCommand {
        if(classroomId == null || classroomId <= 0) {
            throw new IllegalArgumentException("Classroom ID must be a positive number.");
        }
        if(code == null || code.isBlank()) {
            throw new IllegalArgumentException("Classroom code must not be null or blank.");
        }
        if(capacity == null || capacity <= 0) {
            throw new IllegalArgumentException("Classroom capacity must be a positive number.");
        }
        if(campus == null || campus.isBlank()) {
            throw new IllegalArgumentException("Campus must not be null or blank.");
        }
    }
}
