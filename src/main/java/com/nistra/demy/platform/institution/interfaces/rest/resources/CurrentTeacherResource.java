package com.nistra.demy.platform.institution.interfaces.rest.resources;

public record CurrentTeacherResource(
        Long id,
        String firstName,
        String lastName,
        String emailAddress,
        String phoneNumber,
        Long userId
) {
}
