package com.nistra.demy.platform.scheduling.domain.model.commands;

/**
 * Command to create a new course with specific details such as name, code, and description.
 *
 * @param name The name of the course.
 * @param code The unique identifier for the course.
 * @param description A brief description of the course.
 *
 */
public record CreateCourseCommand(String name, String code, String description) {

    public CreateCourseCommand{
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name must not be null or blank");
        }
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Code must not be null or blank");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description must not be null or blank");
        }
    }
}