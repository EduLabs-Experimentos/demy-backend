package com.nistra.demy.platform.scheduling.interfaces.rest.resources;

public record ClassroomResource(
        Long id,
        String code,
        Integer capacity,
        String campus
) {
}