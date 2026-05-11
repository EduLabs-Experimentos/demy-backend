package com.nistra.demy.platform.scheduling.domain.model.commands;

/**
 * Command to delete a classroom based on its unique identifier.
 *
 * @param classroomId The unique identifier of the classroom to be deleted.
 *
 */
public record DeleteClassroomCommand(Long classroomId) {

    public DeleteClassroomCommand {
        if (classroomId == null || classroomId <= 0) {
            throw new IllegalArgumentException("classroomId cannot be null or less than 1");
        }
    }
}