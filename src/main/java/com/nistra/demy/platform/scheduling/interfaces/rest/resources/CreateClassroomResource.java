package com.nistra.demy.platform.scheduling.interfaces.rest.resources;

public record CreateClassroomResource(
        String code,
        Integer capacity,
        String campus
) {
    public CreateClassroomResource {
        if( code == null || code.isBlank() ) {
            throw new IllegalArgumentException("Code must not be null or blank");
        }
        if( capacity == null || capacity <= 0 ) {
            throw new IllegalArgumentException("Capacity must not be null or less than 1");
        }
        if( campus == null || campus.isBlank() ) {
            throw new IllegalArgumentException("Campus must not be null or blank");
        }
    }
}