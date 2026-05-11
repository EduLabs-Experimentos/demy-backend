package com.nistra.demy.platform.scheduling.interfaces.rest.resources;

public record UpdateClassroomResource(
        String code,
        Integer capacity,
        String campus
) {
    public UpdateClassroomResource {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Code is required");
        }
        if (capacity == null || capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be a positive number.");
        }
        if (campus == null || campus.isBlank()) {
            throw new IllegalArgumentException("Campus is required");
        }
    }
}