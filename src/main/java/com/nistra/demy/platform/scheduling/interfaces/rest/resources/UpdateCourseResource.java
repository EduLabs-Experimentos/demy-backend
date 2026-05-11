package com.nistra.demy.platform.scheduling.interfaces.rest.resources;

public record UpdateCourseResource(
        String name,
        String code,
        String description
) {
    public UpdateCourseResource {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name is required.");
        }
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Code is required.");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description is required.");
        }
    }
}