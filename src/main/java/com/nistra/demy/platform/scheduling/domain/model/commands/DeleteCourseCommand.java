package com.nistra.demy.platform.scheduling.domain.model.commands;

/**
 * Command to delete a course.
 * @param courseId ID of the course to delete
 */

public record DeleteCourseCommand(Long courseId) {

    public DeleteCourseCommand {
        if (courseId == null || courseId <= 0) {
            throw new IllegalArgumentException("courseId cannot be null or less than 1");
        }
    }
}