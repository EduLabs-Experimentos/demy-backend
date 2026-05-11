package com.nistra.demy.platform.scheduling.interfaces.rest.resources;

public record CreateCourseResource(
        String name,
        String code,
        String description
) {
    public CreateCourseResource {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Code cannot be null or blank");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description cannot be null or blank");
        }
    }
}