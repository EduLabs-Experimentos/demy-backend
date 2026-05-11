package com.nistra.demy.platform.scheduling.interfaces.rest.resources;


public record CourseResource(
        Long id,
        String name,
        String code,
        String description
) {
}