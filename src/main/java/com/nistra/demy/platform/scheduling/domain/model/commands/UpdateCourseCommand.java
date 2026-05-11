package com.nistra.demy.platform.scheduling.domain.model.commands;

/**
 * Command to update the details of an existing course.
 *
 * @param courseId The unique identifier of the course to be updated.
 * @param name The new name of the course.
 * @param code The new code for the course.
 * @param description The updated description of the course.
 */
public record UpdateCourseCommand(
        Long courseId,
        String name,
        String code,
        String description
) {
    public UpdateCourseCommand {
        if(courseId == null || courseId <= 0) {
            throw new IllegalArgumentException("Course ID must be a positive number.");
        }
        if(name == null || name.isBlank()) {
            throw new IllegalArgumentException("Course name must not be null or blank.");
        }
        if(code == null || code.isBlank()) {
            throw new IllegalArgumentException("Course code must not be null or blank.");
        }
        if(description == null || description.isBlank()) {
            throw new IllegalArgumentException("Course description must not be null or blank.");
        }
    }
}